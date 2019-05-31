package com.lucianbc.receiptscan.domain.model

data class Annotation (
    val text: String,
    val top: Int,
    val bottom: Int,
    val left: Int,
    val right: Int,
    val tag: Tag
)
