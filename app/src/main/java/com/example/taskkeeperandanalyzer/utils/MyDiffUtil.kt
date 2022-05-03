package com.example.taskkeeperandanalyzer.utils

import androidx.recyclerview.widget.DiffUtil
import com.example.taskkeeperandanalyzer.model.TaskModel

class MyDiffUtil(
    private val oldTasks: List<TaskModel>,
    private val newTasks: List<TaskModel>

): DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldTasks.size
    }

    override fun getNewListSize(): Int {
        return newTasks.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldTasks[oldItemPosition].title == newTasks[newItemPosition].title
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return when {
            oldTasks[oldItemPosition].title != newTasks[newItemPosition].title ->{
                false
            }
            oldTasks[oldItemPosition].desc != newTasks[newItemPosition].desc ->{
                false
            }
            oldTasks[oldItemPosition].remainderTime != newTasks[newItemPosition].remainderTime ->{
                false
            }
            else -> {
                true
            }
        }
    }
}