package com.example.taskkeeperandanalyzer.ui.home


import com.example.taskkeeperandanalyzer.model.TaskModel
import com.example.taskkeeperandanalyzer.utils.Resource
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import javax.inject.Inject


class HomeRepository
@Inject
constructor(
    private val fireStoreDb: FirebaseFirestore
){

    suspend fun getAllTasks(
        userId: String,
        taskRootRef: String
    ): Flow<Resource<List<TaskModel>>> = flow{
        emit(Resource.Loading())

        try {

            val snapshots = fireStoreDb.collection(taskRootRef).document(userId).collection(userId)
                .orderBy("creationTimeMs", Query.Direction.DESCENDING)
                .get()
                .await()

            val retrievedTasks = snapshots.toObjects(TaskModel::class.java)

            emit(Resource.Success(retrievedTasks))

        }catch (e: Exception){
            emit(Resource.Error(e))
        }

    }.catch {
        emit(Resource.Error(it))
    }.flowOn( Dispatchers.IO )


}

