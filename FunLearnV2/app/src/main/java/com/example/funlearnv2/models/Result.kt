package com.example.funlearnv2.models

sealed class Result<out V> {

    class Loading<out V> : Result<V>()
    data class Success<out V>(val data: V) : Result<V>()
    data class Failure<out V>(val throwable: Throwable) : Result<V>()
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
