package com.example.cookbook.utils

sealed class RequestResult<out T>{
    class Loading(val data:String) : RequestResult<String>()
    data class Success(val data: Boolean) : RequestResult<Boolean>()
    data class Failure(val throwable: Throwable) : RequestResult<Throwable>()
}