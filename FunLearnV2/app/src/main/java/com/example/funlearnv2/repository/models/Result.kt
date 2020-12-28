package com.example.funlearnv2.repository.models

sealed class Result<out V> {

    data class Value<out V>(val value: V) : Result<V>()
    data class Error(val exception: Exception) : Result<Nothing>()
    companion object {
        inline fun <V> build(function: () -> V): Result<V> = try {
            Value(function.invoke())
        } catch (e: Exception) {
            Error(e)
        }
    }
}
