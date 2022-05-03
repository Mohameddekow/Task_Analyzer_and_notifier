package com.example.taskkeeperandanalyzer.model

data class TaskModel(
    val title: String? = null,
    val desc: String? = null,
    val remainderTime: Long? = null,
    val taskType: String? = null,
    val creationTimeMs: Long? = null,

)
