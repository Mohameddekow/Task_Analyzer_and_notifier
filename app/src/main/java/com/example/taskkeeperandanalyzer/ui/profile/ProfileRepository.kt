package com.example.taskkeeperandanalyzer.ui.profile

import android.net.Uri
import android.util.Log
import com.example.taskkeeperandanalyzer.model.UserModel
import com.example.taskkeeperandanalyzer.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import javax.inject.Inject

class ProfileRepository
@Inject
constructor(
    private val firebaseStorage: FirebaseStorage,
    private val auth: FirebaseAuth,
    private val fireStoreDb: FirebaseFirestore
) {


    //fetch user details
    suspend fun fetchUserDetails(
        userPathRef: String,
        userId: String
    ): Flow<Resource<UserModel>> = flow{

//        delay(1000) //delayed for testing only

      try {
          //start the ui with loading
          emit(Resource.Loading())


          //get the snapshot from fireStore
          val snapshot = fireStoreDb.collection(userPathRef).document(userId)
              .get()
              .await()

          //change to object
          val userDetails = snapshot.toObject(UserModel::class.java)

          Log.d("TAG", "getUserDet: $userDetails")

          //emit it to ui as success
          emit(Resource.Success(userDetails!!))
      }catch (e: Exception){
          emit(Resource.Error(e))
      }


    }.catch {
        //catch any exception
        emit(Resource.Error(it))
    }.flowOn(Dispatchers.IO)  //i can leave as i will be running on Dispatcher.IO on the  viewModel




    //update user profile name only
    suspend fun updateUserProfileNameOnly(
        name: String,
        userId: String,
        usersPathRef: String
    ): Flow<Resource<Void?>> = flow{

        emit(Resource.Loading())

        var taskResult: Void? = null
        var errorMsg: Exception? = null

        try {
            val task = fireStoreDb.collection(usersPathRef).document(userId).update("name", name)

            task.addOnSuccessListener {
                taskResult = it
                Log.d("TAG", "getTask: Within is $it")

            }.await()
            Log.d("TAG", "getTask: Outside $taskResult")


            task.addOnFailureListener {
                errorMsg = it
                Log.d("TAG", "getException: Within is $it")

            }
            Log.d("TAG", "getException: Outside is $errorMsg")


            if (task.isSuccessful){
                emit(Resource.Success(taskResult))
            }else{
                emit(Resource.Error(errorMsg!!))
            }

        }catch (e: Exception){
            emit(Resource.Error(e))
        }


    }.catch {
        emit(Resource.Error(it))
    }.flowOn( Dispatchers.IO )



    //update add user details , pic and names
     suspend fun updateAllUserProfileDetails(
        name: String,
        userId: String,
        usersPathRef: String,
        photoUri: Uri,
        profileImagesRootRef: String
    ): Flow<Resource<Void?>> = flow{

        emit(Resource.Loading())

        val email = auth.currentUser?.email
        val photoStoragePathRef = firebaseStorage.reference.child("${profileImagesRootRef}/${userId}")

        var taskResult: Void? = null
        var errorMsg: Exception? = null

        try {
            val task = photoStoragePathRef.putFile(photoUri)
                .continueWithTask { photoUploadTask ->
                    photoStoragePathRef.downloadUrl //makes the download url available
                }
                .continueWithTask { downloadUrlTask ->
                    val downloadUrl =
                        downloadUrlTask.result.toString() //image url will now be the download url of firebase

                    //add the photo url to fireStore under user's id
                    fireStoreDb.collection(usersPathRef).document(userId)
                        .set(UserModel(name, email!!, downloadUrl))
                }


            task.addOnSuccessListener {
                taskResult = it
            }.await()

            task.addOnFailureListener {
                errorMsg = it
            }

            if (task.isSuccessful){
                emit(Resource.Success(taskResult))
            }else{
                emit(Resource.Error(errorMsg!!))
            }


        }catch (e: Exception){
            emit(Resource.Error(e))
        }

    }.catch {
        emit(Resource.Error(it))
    }.flowOn( Dispatchers.IO )

}
