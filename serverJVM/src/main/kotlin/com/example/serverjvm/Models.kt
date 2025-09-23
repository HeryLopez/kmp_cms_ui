package com.example.serverjvm

import kotlinx.serialization.Serializable

@Serializable
data class ComponentItem(
    val text: String,
    val color: String,
    val type: String
)
