package com.worldwidemobiledevelopment.recipes.view.meal

import androidx.lifecycle.ViewModel
import com.worldwidemobiledevelopment.recipes.data.Comment
import com.worldwidemobiledevelopment.recipes.data.CookingStep

class MealViewModel : ViewModel(){

    val mealImages = arrayOf("https://worldofmeat.ru/wp-content/uploads/2021/02/1598133062_barbecued-herb-lamb-cmyk.jpg",
        "https://2.bp.blogspot.com/-nz6SjwwaIXA/WekiFYkd5LI/AAAAAAAADZU/eOJXR8uyt3gmBe2ILUeP4-GCqC7Mo-xPgCLcBGAs/s1600/Forerib-of-beef.jpg",
        "https://eda-land.ru/images/article/orig/2018/10/kak-gotovit-prostye-i-slozhnye-blyuda-iz-svininy.jpg")

    val ingredientsList = listOf("Мука" to "200 гр", "Картошка" to "400 гр", "Мука" to "200 гр", "Картошка" to "400 гр","Мука" to "200 гр", "Картошка" to "400 гр")

    val cookingSteps = listOf(
        CookingStep("Возьмите 5 ломтиков хлеба и выложите их в сковороду"),
        CookingStep("Возьмите 5 ломтиков хлеба и выложите их в сковороду"),
        CookingStep("Возьмите 5 ломтиков хлеба и выложите их в сковороду"),
        CookingStep("Возьмите 5 ломтиков хлеба и выложите их в сковороду"),
        CookingStep("Возьмите 5 ломтиков хлеба и выложите их в сковороду"),
        CookingStep("Возьмите 5 ломтиков хлеба и выложите их в сковороду"),
        CookingStep("Возьмите 5 ломтиков хлеба и выложите их в сковороду"),
        CookingStep("Возьмите 5 ломтиков хлеба и выложите их в сковороду"),
        CookingStep("Возьмите 5 ломтиков хлеба и выложите их в сковороду"),
        CookingStep("Возьмите 5 ломтиков хлеба и выложите их в сковороду"),
        CookingStep("Возьмите 5 ломтиков хлеба и выложите их в сковороду"),
        )

//    val commetns = listOf(
//        Comment("121212sdfsd", "Анна Федоровна Фатхлисламова", "Начинающий повар", null, 5, "q13eds1313","Решила побаловать себя после работы. На приготовление ушло меньше 10 миунт. Вкус лучше чем в ресторанах Michelin!  Всем советую!", "21.21.21"),
//        Comment("121212sdfsd", "Анна Федоровна Фатхлисламова", "Начинающий повар", null, 4, "q13eds1313","Решила побаловать себя после работы. На приготовление ушло меньше 10 миунт. Вкус лучше чем в ресторанах Michelin!  Всем советую!", "21.21.21"),
//        Comment("121212sdfsd", "Анна Федоровна Фатхлисламова", "Начинающий повар", null, 3, "q13eds1313","Решила побаловать себя после работы. На приготовление ушло меньше 10 миунт. Вкус лучше чем в ресторанах Michelin!  Всем советую!", "21.21.21"),
//        Comment("121212sdfsd", "Анна Федоровна Фатхлисламова", "Начинающий повар", null, 2, "q13eds1313","Решила побаловать себя после работы. На приготовление ушло меньше 10 миунт. Вкус лучше чем в ресторанах Michelin!  Всем советую!", "21.21.21"),
//    )


}