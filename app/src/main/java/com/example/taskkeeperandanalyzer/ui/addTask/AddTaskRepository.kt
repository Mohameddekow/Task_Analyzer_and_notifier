package com.example.taskkeeperandanalyzer.ui.addTask

import android.util.Log
import com.example.taskkeeperandanalyzer.model.AddTaskModel
import com.example.taskkeeperandanalyzer.utils.Resource
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import java.util.*
import javax.inject.Inject


class AddTaskRepository
@Inject
constructor(
    private val fireStoreDb: FirebaseFirestore
){

    //Getting the current date
    private var date: Date = Date()

    //This method returns the time in millis
    private var timeMilli: Long = date.getTime()

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
                .set(AddTaskModel(title, desc, remainderTime, taskType))


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