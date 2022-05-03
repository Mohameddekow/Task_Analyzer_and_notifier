package com.example.taskkeeperandanalyzer.ui.addTask

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskkeeperandanalyzer.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AddTaskViewModel
@Inject
constructor(
    private val repository: AddTaskRepository
): ViewModel(){

    private val _addingTaskState: MutableLiveData<Resource<Void?>> = MutableLiveData()
    val addingTaskState: LiveData<Resource<Void?>> get() = _addingTaskState



    fun addTaskToFireStore(
        userId: String,
        taskRootRef: String,
        title: String,
        desc: String,
        remainderTime: Long,
        taskType: String
    ){
        viewModelScope.launch(Dispatchers.IO){
            val result = repository.addTaskToFireStore(userId, taskRootRef, title, desc, remainderTime, taskType)

            try {
                result.collect {
                    _addingTaskState.postValue(it)
                }
            }catch (e: Exception){
                _addingTaskState.postValue(Resource.Error(e))
            }
        }
    }
}