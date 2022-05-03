package com.example.taskkeeperandanalyzer.ui.addTask

import android.util.Log
import com.example.taskkeeperandanalyzer.model.TaskModel
import com.example.taskkeeperandanalyzer.utils.Resource
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class AddTaskRepository
@Inject
constructor(
    private val fireStoreDb: FirebaseFirestore
){

    suspend fun addTaskToFireStore(
        userId: String,
        taskRootRef: String,
        title: String,
        desc: String,
        remainderTime: Long,
        taskType: String
    ): Flow<Resource<Void?>> = flow{

        emit(Resource.Loading())

        try {
            var taskResult: Void? = null
            var errorMsg : Exception? = null

            val task = fireStoreDb.collection(taskRootRef).document(userId).collection(userId) // from taskRet/uid/uid
                .document() //auto generated document ref
                .set(TaskModel(title, desc, remainderTime, taskType, System.currentTimeMillis()))


            task.addOnSuccessListener {
                taskResult = it
            }.await()

            task.addOnFailureListener {
                errorMsg = it
            }

            Log.d("TAG", "getTaskAdded : ${task.isSuccessful}")

            if (task.isSuccessful){
                emit(Resource.Success(taskResult))
            }else{
                emit(Resource.Error(errorMsg!!))
            }

        }catch (e:Exception){
            emit(Resource.Error(e))
        }

    }.catch {
        emit(Resource.Error(it))
    }.flowOn( Dispatchers.IO )


}