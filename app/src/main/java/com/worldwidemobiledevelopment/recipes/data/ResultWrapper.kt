package com.worldwidemobiledevelopment.recipes.data


/**
* Wrapper for operations, which can return errors
*/

enum class ResultStatus {
    SUCCESS,
    ERROR
}

data class ResultWrapper<out T>(val status: ResultStatus, val message: String?, val data: T?, val exception: Exception?) {
    companion object {
        fun <T> success( message: String? = null, data: T? = null): ResultWrapper<T> {
            return ResultWrapper(ResultStatus.SUCCESS, message, data, null)
        }

        fun <T> error(message : String? = null, exception: Exception? = null): ResultWrapper<T> {
            return ResultWrapper(ResultStatus.ERROR, message, null, exception)
        }
    }
}



//sealed class OperationResult
//
//data class Success<T>(
//    val message: String? = null,
//    val data: T?
//) : OperationResult()
//
//data class Error(
//    val message: String? = null,
//    val exception: Exception?,
//) : OperationResult()
//
