package com.test.fitnessstudios.repository

sealed class Resource<out R> {
    data class Success<out R>(val result: R) : Resource<R>()
    data class Error(val errorMsg: String) : Resource<Nothing>()
    object Loading : Resource<Nothing>()
}
