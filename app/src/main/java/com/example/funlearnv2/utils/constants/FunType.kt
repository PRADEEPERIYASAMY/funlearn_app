package com.example.funlearnv2.utils.constants

enum class FunType(val type: String) {
    ALPHABETS("alphabets"),
    MATHS("maths"),
    PAINT("paint"),
    GAME("game");

    override fun toString(): String = type
}
