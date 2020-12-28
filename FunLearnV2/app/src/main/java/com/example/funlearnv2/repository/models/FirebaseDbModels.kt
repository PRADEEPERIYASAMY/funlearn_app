package com.example.funlearnv2.repository.models

import androidx.annotation.Keep
import com.google.firebase.database.IgnoreExtraProperties

@Keep
@IgnoreExtraProperties
data class FirebaseDbModels(
    var AlphabetImagesAndNames: AlphabetImagesAndNames? = null,
    var AlphabetPhrases: ArrayList<String>? = null,
    var AlphabetWords: AlphabetWords? = null,
    var ListOfAlphabets: ListOfAlphabets? = null,
    var Match: Match? = null,
    var OperatorImages: String? = null,
)

@Keep
@IgnoreExtraProperties
data class AlphabetImagesAndNames(
    var Images: ArrayList<String>? = null,
    var Names: ArrayList<String>? = null,
)

@Keep
@IgnoreExtraProperties
data class AlphabetWords(
    var Images: ArrayList<String>? = null,
    var Words: ArrayList<String>? = null,
)

@Keep
@IgnoreExtraProperties
data class ListOfAlphabets(
    var Words: String? = null,
)

@Keep
@IgnoreExtraProperties
data class Match(
    var Images: String? = null,
    var Names: String? = null,
)
