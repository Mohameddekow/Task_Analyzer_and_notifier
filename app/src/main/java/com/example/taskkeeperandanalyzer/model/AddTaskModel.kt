package com.example.taskkeeperandanalyzer.model

data class AddTaskModel(
    val title: String? = null,
    val desc: String? = null,
    val remainderTime: Long? = null,
    val taskType: String? = null
)
