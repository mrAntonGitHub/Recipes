package com.worldwidemobiledevelopment.recipes.repository.firebaseRepository

import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.worldwidemobiledevelopment.recipes.Application
import com.worldwidemobiledevelopment.recipes.data.*
import com.worldwidemobiledevelopment.recipes.data.room.entity.User
import com.worldwidemobiledevelopment.recipes.utils.Utils
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton


/**
 * recipes
 *  - userId
 *      - recipeId...
 *  - userId
 *      - recipeId...
 *
 * users
 *  - recipes
 *      - meal id
 *      - meal id
 *      - ...
 **/

const val USER_PATH = "users"
const val ADMIN_PATH = "admin"
const val RECIPES_PATH = "recipes"
const val MEALS_PATH = "meals"
const val IMAGES_PATH = "images"

const val RECIPE_IMAGE_PATH = "recipeImages"

interface FirebaseRepository {
    suspend fun getUser(): ResultWrapper<User>
    suspend fun getUser(uid: String): ResultWrapper<User>
    suspend fun getUsers(vararg uid: String): ResultWrapper<List<User>>

    suspend fun setAvatar(uri: Uri, userId: String): ResultWrapper<String>
    suspend fun getRecipe(authorId: String, recipeId: String): ResultWrapper<Recipe>

    suspend fun publishRecipe(recipe: Recipe) : ResultWrapper<Nothing>

    /**
     * Firebase Authorization
     **/
    suspend fun signUp(email: String, password: String): ResultWrapper<User>
    suspend fun signIn(email: String, password: String): ResultWrapper<User>
    suspend fun signIn(idToken: String): ResultWrapper<User>
    suspend fun verifyUserEmail(): ResultWrapper<Nothing>
    suspend fun removeUser(): ResultWrapper<Nothing>
    suspend fun isEmailVerified(): ResultWrapper<Boolean>
    suspend fun resetPassword(email: String): ResultWrapper<Nothing>
    fun signOut()
}


@Singleton
class FirebaseRepositoryImpl @Inject constructor(private val iFirebaseAuthorization: IFirebaseAuthorization) :
    FirebaseRepository {

    init {
        Application.application.appComponent.inject(this)
    }

    private val fireStore = FirebaseFirestore.getInstance()
    var fireStorage = FirebaseStorage.getInstance().reference

    // Paths
    private val pathUser = fireStore.collection(USER_PATH)
    private val pathAdmin = fireStore.collection(ADMIN_PATH)
    private val pathRecipes = fireStore.collection(RECIPES_PATH)

    private val pathUserRecipe = fireStore.collection(RECIPES_PATH)
    private val pathAdminRecipe =
        fireStore.collection(RECIPES_PATH).document(ADMIN_PATH).collection(
            MEALS_PATH
        )


    override suspend fun getUser(): ResultWrapper<User> {
        /**
         * @return User instance from fireStore
         **/
        return CompletableDeferred<ResultWrapper<User>>().apply {
            val userId = iFirebaseAuthorization.userId()
            if (userId.status == ResultStatus.SUCCESS && userId.data != null) {
                pathUser.document(userId.data).get().addOnCompleteListener {
                    if (it.isSuccessful) {
                        Log.d("FirebaseRepository", "Got user")
                        Log.e("FirebaseRepository", "${it.result}")
                        Log.e("FirebaseRepository", "${it.result?.data}")
                        Log.e("FirebaseRepository", "${it.result?.data}")
                        Log.e("FirebaseRepository", "${it.result?.toObject(User::class.java)}")
                        this.complete(ResultWrapper.success(data = it.result?.toObject(User::class.java)))
                    } else {
                        Log.e("FirebaseRepository", "${it.exception}")
                        this.complete(
                            ResultWrapper.error(
                                message = "Ощибка загрузки профиля",
                                exception = it.exception
                            )
                        )
                    }
                }
            } else {
                Log.e("FirebaseRepository", "${userId.exception}")
                this.complete(
                    ResultWrapper.error(
                        message = "Ощибка загрузки профиля",
                        exception = userId.exception
                    )
                )
            }
        }.await()
    }

    override suspend fun getUser(uid: String): ResultWrapper<User> {
        TODO("Not yet implemented")
    }

    override suspend fun getUsers(vararg uid: String): ResultWrapper<List<User>> {
        TODO("Not yet implemented")
    }

    override suspend fun setAvatar(uri: Uri, userId: String): ResultWrapper<String> {
        /**
         * Add image in firebase Storage
         * @return uri path
         **/
        return CompletableDeferred<ResultWrapper<String>>().apply {
            val storageReference = fireStorage.child("images/$userId/avatar")
            storageReference.putFile(uri)
                .addOnSuccessListener {
                    it.storage.downloadUrl
                        .addOnSuccessListener {
                            CoroutineScope(IO).launch {
                                if (changeAvatarUri(it)) {
                                    Log.d("FirebaseRepository", "Avatar changed")
                                    Log.d(
                                        "FirebaseRepository",
                                        "path: ${storageReference.downloadUrl}"
                                    )
                                    this@apply.complete(ResultWrapper.success(data = storageReference.path))
                                } else {
                                    Log.e(
                                        "FirebaseRepository",
                                        "Error while changing avatar URI in profile"
                                    )
                                    this@apply.complete(ResultWrapper.error(message = "Не удалось изменить картинку"))
                                }
                            }
                        }
                        .addOnFailureListener {
                            Log.e("FirebaseRepository", "Can't get download URL")
                            this@apply.complete(ResultWrapper.error(message = "Не удалось изменить картинку"))
                        }
                }
                .addOnFailureListener { exception ->
                    Log.e("FirebaseRepository", "$exception")
                    this.complete(ResultWrapper.error(message = "Не удалось изменить картинку"))
                }

        }.await()
    }

    override suspend fun getRecipe(authorId: String, recipeId: String): ResultWrapper<Recipe> {
        return CompletableDeferred<ResultWrapper<Recipe>>().apply {
            fireStore.collection(RECIPES_PATH).document(authorId).collection(recipeId).document()
                .get()
                .addOnSuccessListener {
                    this.complete(ResultWrapper.success(data = it.toObject(Recipe::class.java)))
                }
                .addOnFailureListener {
                    Log.e("FirebaseRepository", it.toString())
                    this.complete(ResultWrapper.error("Ошибка загрузки рецепта", exception = it))
                }
        }.await()
    }

    override suspend fun publishRecipe(recipe: Recipe): ResultWrapper<Nothing> {
        return CompletableDeferred<ResultWrapper<Nothing>>().apply {
            val completableDeferred = this
            val currentUser = getUser()
            when(currentUser.status){
                ResultStatus.SUCCESS -> {
                    val user = currentUser.data!!
                    pathUserRecipe.document(user.id).collection(RECIPES_PATH).add("" to "")
                        .addOnSuccessListener { documentReference ->
                            val documentId = documentReference.id
                            CoroutineScope(IO).launch {
                                val imagePath = fireStorage.child(IMAGES_PATH).child(user.id).child(documentId).child(RECIPE_IMAGE_PATH)
                                val recipeImagesLoader = recipe.images?.map { it.toUri() }?.let { uploadImages(imagePath, it) }
                                when(recipeImagesLoader?.status){
                                    ResultStatus.SUCCESS -> {
                                        recipe.steps.forEachIndexed { index, cookingStep ->
                                            val stepImagesSet = cookingStep.imageUrlSet
                                            if (!stepImagesSet.isNullOrEmpty()){
                                                val reference = fireStorage.child(IMAGES_PATH).child(user.id).child(documentId).child(RECIPE_IMAGE_PATH).child(index.toString())
                                                val stepImagesLoader = uploadImages(reference, stepImagesSet.map { it.toUri() })
                                                when(stepImagesLoader.status){
                                                    ResultStatus.SUCCESS -> {
                                                        recipe.steps[index].imageUrlSet = stepImagesLoader.data?.map { it.toString() }?.toMutableList()
                                                    }
                                                    ResultStatus.ERROR -> {
                                                        Log.e("FirebaseRepository", "Error while uploading step recipe images + ${recipeImagesLoader.exception}")
                                                        completableDeferred.complete(ResultWrapper.error(message = recipeImagesLoader.message, exception = Exception("Error while uploading step recipe images + ${recipeImagesLoader.exception}")))
                                                    }
                                                }
                                            }
                                        }
                                        val finalRecipe = Recipe(documentId, Follower(user.id, user.name, user.avatar, user.progress), user.name,
                                            recipeImagesLoader.data?.map { it.toString() }, recipe.description, recipe.steps, recipe.portions_number,
                                            recipe.additional_info, recipe.complexity, recipe.category, recipe.ingredients_list, recipe.tags_diet,
                                            recipe.cooking_minutes, recipe.grams, recipe.kcal, recipe.proteins, recipe.fats, recipe.carbohydrates,
                                            0, Utils.getUnixTime(), null)

                                        pathUserRecipe.document(user.id).collection("recipes").document(documentId).set(finalRecipe)
                                    }
                                    ResultStatus.ERROR -> {
                                        Log.e("FirebaseRepository", "Error while uploading recipe images + ${recipeImagesLoader.exception}")
                                        completableDeferred.complete(ResultWrapper.error(message = recipeImagesLoader.message, exception = Exception("Error while uploading recipe images + ${recipeImagesLoader.exception}")))
                                    }
                                    null -> {  }
                                }
                            }
                        }
                        .addOnFailureListener {
                            Log.e("FirebaseRepository", "Can't create document")
                            this.complete(ResultWrapper.error())
                        }
                }
                ResultStatus.ERROR -> {
                    Log.e("FirebaseRepository", "Can't get current user + ${currentUser.exception}")
                    this.complete(ResultWrapper.error(exception = Exception("Can't get current user + ${currentUser.exception}")))
                }
            }
        }.await()
    }


    private suspend fun uploadImages(path: StorageReference, images: List<Uri>): ResultWrapper<List<Uri>> {
        return CompletableDeferred<ResultWrapper<List<Uri>>>().apply {
            val imageUris = mutableListOf<Uri>()
            images.forEachIndexed { index, uri ->
                path.child(index.toString()).putFile(uri)
                    .addOnSuccessListener { task ->
                        task.storage.downloadUrl
                            .addOnSuccessListener { uri ->
                                imageUris.add(uri)
                                if (index == images.size - 1){
                                    this.complete(ResultWrapper.success(data = imageUris))
                                }
                            }
                            .addOnFailureListener {
                                Log.e("FirebaseRepository", "Can't get download URL")
                                this.complete(ResultWrapper.error(exception = it))
                            }
                    }
                    .addOnFailureListener { exception ->
                        Log.e("FirebaseRepository", "$exception")
                        this.complete(ResultWrapper.error(message = "Не удалось добавить картинки", exception = exception))
                    }
            }
        }.await()
    }

    private suspend fun changeAvatarUri(uri: Uri): Boolean {
        val user = getUser()
        return when (user.status) {
            ResultStatus.SUCCESS -> {
                return when (user.data?.userType) {
                    UserType.SIMPLE -> {
                        pathUser.document(user.data.id).update("avatar", uri.toString())
                        true
                    }
                    UserType.ADMIN -> {
                        pathAdmin.document(user.data.id).set("avatar" to uri.toString())
                        true
                    }
                    else -> {
                        false
                    }
                }
            }
            ResultStatus.ERROR -> {
                false
            }
        }
    }

    // Get certain user's or admin's recipes by uid
    suspend fun getUserRecipes(userId: String): ResultWrapper<List<Recipe>?> {
        // @return list with recipes or null if error or no user's recipes found
        return CompletableDeferred<ResultWrapper<List<Recipe>?>>().apply {
            val usersRecipesId = getUserRecipesIds(userId)
            if (usersRecipesId.status == ResultStatus.SUCCESS) {
                val recipes = mutableListOf<Recipe>()
                usersRecipesId.data?.forEach { recipeId ->
                    pathUserRecipe.document(recipeId).get().addOnCompleteListener {
                        if (it.isSuccessful) {
                            val result = it.result?.toObject(Recipe::class.java)
                            if (result != null) {
                                recipes.add(result)
                            } else {
                                Log.e(
                                    "FirebaseRepository",
                                    "Can't find recipe with id = $recipeId in recipes/users directory. Recipe will be skipped"
                                )
                            }
                        } else {
                            Log.e(
                                "FirebaseRepository",
                                "Error while getting user's recipes with id = $recipeId"
                            )
                        }
                    }
                }
                this.complete(ResultWrapper.success(data = recipes))
            } else {
                this.complete(
                    ResultWrapper.error(
                        "Ошибка получения Id блюд", exception = Exception(
                            "Error while getting $userId recipes. ${usersRecipesId.exception}"
                        )
                    )
                )
            }
        }.await()
    }

    private suspend fun getUserRecipesIds(userId: String): ResultWrapper<List<String>?> {
        return CompletableDeferred<ResultWrapper<List<String>>>().apply {
            pathUser.document(userId).collection(MEALS_PATH).get().addOnCompleteListener {
                if (it.isSuccessful) {
                    val result = it.result?.toObjects(String::class.java)
                    this.complete(ResultWrapper.success(data = result))
                } else {
                    this.complete(ResultWrapper.error(exception = it.exception))
                }
            }
        }.await()
    }

    suspend fun getAdminRecipes(userId: String): ResultWrapper<List<Recipe>?> {
        // @return list with recipes or null if error or no user's recipes found
        return CompletableDeferred<ResultWrapper<List<Recipe>?>>().apply {
            val usersRecipesId = getUserRecipesIds(userId)
            if (usersRecipesId.status == ResultStatus.SUCCESS) {
                val recipes = mutableListOf<Recipe>()
                usersRecipesId.data?.forEach { recipeId ->
                    pathAdminRecipe.document(recipeId).get().addOnCompleteListener {
                        if (it.isSuccessful) {
                            val result = it.result?.toObject(Recipe::class.java)
                            if (result != null) {
                                recipes.add(result)
                            } else {
                                Log.e(
                                    "FirebaseRepository",
                                    "Can't find recipe with id = $recipeId in recipes/users directory. Recipe will be skipped"
                                )
                            }
                        } else {
                            Log.e(
                                "FirebaseRepository",
                                "Error while getting user's recipes with id = $recipeId"
                            )
                        }
                    }
                }
                this.complete(ResultWrapper.success(data = recipes))
            } else {
                this.complete(
                    ResultWrapper.error(
                        "Ошибка получения Id блюд", exception = Exception(
                            "Error while getting $userId recipes. ${usersRecipesId.exception}"
                        )
                    )
                )
            }
        }.await()
    }

    private suspend fun getAdminRecipesIds(userId: String): ResultWrapper<List<String>?> {
        return CompletableDeferred<ResultWrapper<List<String>>>().apply {
            pathAdmin.document(userId).collection(MEALS_PATH).get().addOnCompleteListener {
                if (it.isSuccessful) {
                    val result = it.result?.toObjects(String::class.java)
                    this.complete(ResultWrapper.success(data = result))
                } else {
                    this.complete(ResultWrapper.error(exception = it.exception))
                }
            }
        }.await()
    }

    // Get all recipes as pair @first - users recipes @second - admins recipes
    suspend fun getAllRecipes(): ResultWrapper<Pair<List<Recipe>?, List<Recipe>?>> {
        // @return pair of users and admins recipes
        // @first - users recipes
        // @second - admins recipes
        val usersRecipes = getUsersRecipes()
        val adminsRecipes = getAdminsRecipes()

        return CompletableDeferred<ResultWrapper<Pair<List<Recipe>?, List<Recipe>?>>>().apply {
            if (usersRecipes.status == ResultStatus.SUCCESS || adminsRecipes.status == ResultStatus.SUCCESS) {
                this.complete(
                    ResultWrapper.success(
                        data = Pair(
                            usersRecipes.data,
                            adminsRecipes.data
                        )
                    )
                )
            } else {
                this.complete(
                    ResultWrapper.error(
                        "Рецепты не найдены",
                        exception = Exception("No recipes found")
                    )
                )
            }
        }.await()
    }

    private suspend fun getUsersRecipes(): ResultWrapper<List<Recipe>?> {
        // @return all recipes from recipes/users/meals/....
        return CompletableDeferred<ResultWrapper<List<Recipe>?>>().apply {
            pathUserRecipe.get().addOnCompleteListener {
                if (it.isSuccessful) {
                    val result = it.result?.toObjects(Recipe::class.java)
                    if (result != null) {
                        this.complete(ResultWrapper.success(data = result))
                    } else {
                        this.complete(ResultWrapper.success("Нет доступных рецептов", data = null))
                    }
                } else {
                    this.complete(ResultWrapper.error(exception = it.exception))
                }
            }
        }.await()
    }

    private suspend fun getAdminsRecipes(): ResultWrapper<List<Recipe>?> {
        // return all recipes from recipes/admins/meals/....
        return CompletableDeferred<ResultWrapper<List<Recipe>?>>().apply {
            pathAdminRecipe.get().addOnCompleteListener {
                if (it.isSuccessful) {
                    val result = it.result?.toObjects(Recipe::class.java)
                    if (result != null) {
                        this.complete(ResultWrapper.success(data = result))
                    } else {
                        this.complete(ResultWrapper.success("Нет доступных рецептов", data = null))
                    }
                } else {
                    this.complete(ResultWrapper.error(exception = it.exception))
                }
            }
        }.await()
    }

    //** PUT **//
//    suspend fun pushRecipe(recipeRight: RecipeRight) : ResultWrapper<Nothing>{
//        return CompletableDeferred<ResultWrapper<Nothing>>().apply {
//            val currentUser = currentUser()
//            if (currentUser.status == ResultStatus.SUCCESS){
//                val userType = getUserType(currentUser.data?.uid!!)
//                if (userType.data == UserType.ADMIN){
//                    pathAdminRecipe.add(recipeRight).addOnCompleteListener {
//                        if (it.isSuccessful){
//                            val id = it.result?.id
//                            if (!id.isNullOrEmpty()){
//                                pathAdmin.document(currentUser.data.uid).collection(MEALS_PATH).document(id)
//                                this.complete(ResultWrapper.success())
//                            }else{
//                                this.complete(ResultWrapper.error(exception = Exception("Error while getting recipe ID")))
//                            }
//                        }else{
//                            this.complete(ResultWrapper.error("Не удалось опубликовать рецепт", Exception("Error while publishing recipe = $recipeRight", it.exception)))
//                        }
//                    }
//                }else {
//                    pathUserRecipe.add(recipeRight).addOnCompleteListener {
//                        if (it.isSuccessful){
//                            val id = it.result?.id
//                            if (!id.isNullOrEmpty()){
//                                pathUser.document(currentUser.data.uid).collection(MEALS_PATH).document(id)
//                                this.complete(ResultWrapper.success())
//                            }else{
//                                this.complete(ResultWrapper.error(exception = Exception("Error while getting recipe ID")))
//                            }
//                        }else{
//                            this.complete(ResultWrapper.error("Не удалось опубликовать рецепт", Exception("Error while publishing recipe = $recipeRight", it.exception)))
//                        }
//                    }
//                }
//            }else{
//                this.complete(ResultWrapper.error("Ошибка авторизации", exception = currentUser.exception))
//            }
//        }.await()
//    }
    private suspend fun getUserType(userId: String): ResultWrapper<UserType> {
        return CompletableDeferred<ResultWrapper<UserType>>().apply {
            val userPath = pathUser.document(userId).get().result
            val adminPath = pathAdmin.document(userId).get().result
            if (userPath != null) {
                if (userPath.exists()) {
                    this.complete(ResultWrapper.success(data = UserType.SIMPLE))
                }
            } else if (adminPath != null) {
                if (adminPath.exists()) {
                    this.complete(ResultWrapper.success(data = UserType.ADMIN))
                }
            } else {
                this.complete(
                    ResultWrapper.error(
                        "Не удалось получить тип пользователя", exception = Exception(
                            "Can't get user type. AdminPath = $adminPath UserPath = $userPath"
                        )
                    )
                )
            }
        }.await()
    }


    /**
     * Firebase Authorization
     **/
    override suspend fun signUp(email: String, password: String): ResultWrapper<User> {
        val result = signUpWithEmailAndPassword(email, password)
        if (result.status == ResultStatus.SUCCESS) {
            result.data?.let { createUserDirectory(it) }
        }
        return result
    }

    override suspend fun signIn(email: String, password: String): ResultWrapper<User> {
        val result = signInWithEmailAndPassword(email, password)
        if (result.status == ResultStatus.SUCCESS) {
            result.data?.let { createUserDirectory(it) }
        }
        return result
    }

    override suspend fun signIn(idToken: String): ResultWrapper<User> {
        val result = signInWithGoogle(idToken)
        if (result.status == ResultStatus.SUCCESS) {
            result.data?.let { createUserDirectory(it) }
        }
        return result
    }

    override suspend fun verifyUserEmail(): ResultWrapper<Nothing> {
        return iFirebaseAuthorization.verifyUserEmail()
    }

    override suspend fun removeUser(): ResultWrapper<Nothing> {
        /**
         * Remove
         *  - User's directory
         *  - User's authorization
         *  - User's recipes
         **/
        return CompletableDeferred<ResultWrapper<Nothing>>().apply {
            val userUid = iFirebaseAuthorization.userId()
            if (userUid.status == ResultStatus.SUCCESS) {
                pathUser.document(userUid.data.toString()).delete()
                val removeUserAuthorization = iFirebaseAuthorization.removeUserAuthorization()
                if (removeUserAuthorization.status == ResultStatus.SUCCESS) {
                    this.complete(ResultWrapper.success())
                    // TODO Добвать удаление рецептов пользователя
                } else {
                    Log.e(
                        "FirebaseRepository",
                        "User's path was deleted, but can't remove user's authorization data due to exception: ${removeUserAuthorization.exception}"
                    )
                    this.complete(
                        ResultWrapper.error(
                            message = "Ошибка удаления",
                            exception = removeUserAuthorization.exception
                        )
                    )
                }
            } else {
                Log.e(
                    "FirebaseRepository",
                    "Can't remove user due to exception: Can't get user UID"
                )
                this.complete(
                    ResultWrapper.error(
                        message = "Ошибка удаления", exception = Exception(
                            "Can't get user UID"
                        )
                    )
                )
            }
        }.await()

    }

    override suspend fun isEmailVerified(): ResultWrapper<Boolean> {
        return iFirebaseAuthorization.isEmailVerified()
    }

    override suspend fun resetPassword(email: String): ResultWrapper<Nothing> {
        return iFirebaseAuthorization.resetPassword(email)
    }

    override fun signOut() {
        iFirebaseAuthorization.signOut()
    }

    private fun createUserDirectory(user: User) {
        pathUser.document(user.id).get().addOnSuccessListener {
            if (!it.exists()) {
                val avatar = user.avatar
                pathUser.document(user.id).set(user).addOnSuccessListener {
                    Log.d("FirebaseRepository", "User's directory created")
                }

            } else {
                Log.d("FirebaseRepository", "User's directory already exists")
            }
        }
    }

    private suspend fun signUpWithEmailAndPassword(
        email: String,
        password: String
    ): ResultWrapper<User> {
        return iFirebaseAuthorization.signUpWithEmailAndPassword(email, password)
    }

    private suspend fun signInWithEmailAndPassword(
        email: String,
        password: String
    ): ResultWrapper<User> {
        return iFirebaseAuthorization.signInWithEmailAndPassword(email, password)
    }

    private suspend fun signInWithGoogle(idToken: String): ResultWrapper<User> {
        return iFirebaseAuthorization.signInWithGoogle(idToken)
    }
}