package com.example.taskkeeperandanalyzer.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskkeeperandanalyzer.model.TaskModel
import com.example.taskkeeperandanalyzer.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel
@Inject
constructor(
    private val repository: HomeRepository
): ViewModel() {

    private val _fetchingTasksState: MutableLiveData<Resource<List<TaskModel>>> = MutableLiveData()
    val fetchingTasksState: LiveData<Resource<List<TaskModel>>> get() = _fetchingTasksState


 //fetch all tasks
    fun getAllTasks(
        userId: String,
        taskRootRef: String
    ){
        viewModelScope.launch (){

            val result =  repository.getAllTasks(userId, taskRootRef)

            try {
                result.collect {
                    _fetchingTasksState.postValue(it)
                }

            }catch (e: Exception){
                // Resource.Error(e, null)
                _fetchingTasksState.postValue(Resource.Error(e, null))
            }

        }
    }


}