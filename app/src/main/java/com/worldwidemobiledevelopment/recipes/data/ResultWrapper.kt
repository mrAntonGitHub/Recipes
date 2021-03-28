package com.worldwidemobiledevelopment.recipes.data


/**
* Wrapper for operations, which can return errors
*/

enum class ResultStatus {
    SUCCESS,
    ERROR
}

data class ResultWrapper<out T>(val status: ResultStatus, val message: String?, val data: T?, val error: Exception?) {
    companion object {
        fun <T> success( message: String? = null, data: T? = null): ResultWrapper<T> {
            return ResultWrapper(ResultStatus.SUCCESS, message, data, null)
        }

        fun <T> error(message : String? = null, error: Exception? = null): ResultWrapper<T> {
            return ResultWrapper(ResultStatus.ERROR, message, null, error)
        }
    }
}