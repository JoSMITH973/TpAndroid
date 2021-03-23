package com.example.tpjoansmithleolemercier.tasklist

import kotlinx.serialization.SerialName
import java.io.Serializable
@kotlinx.serialization.Serializable
data class Task(
    @SerialName("id")
    val id: String,
    @SerialName("title")
    val title: String,
    @SerialName("description")
    val description: String = "C'est la description"
): Serializable
