package com.example.taskkeeperandanalyzer.ui.profile

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.taskkeeperandanalyzer.model.UserModel
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.QuerySnapshot
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val  repository: ProfileRepository
): ViewModel() {

    private val _userDetails: MutableLiveData<UserModel> = MutableLiveData()
    val userDetails: LiveData<UserModel> get() = _userDetails

//    private val _queryStateError: MutableLiveData<Boolean> = MutableLiveData()
//    val queryStateError: LiveData<Boolean> get() = _queryStateError


    //update user profile
    fun updateAllUserProfileDetails(
        name: String,
        userId: String,
        usersPathRef: String,
        photoUri: Uri,
        profileImagesRootRef: String
    ): Task<Void> {
        return repository.updateAllUserProfileDetails(
            name,
            userId,
            usersPathRef,
            photoUri,
            profileImagesRootRef)
    }

    //update user profile name only
    fun updateUserProfileNameOnly(
        name: String,
        userId: String,
        usersPathRef: String
    ): Task<Void> {
        return repository.updateUserProfileNameOnly(name, userId, usersPathRef)
    }




    //fetch user details
    fun fetchUserDetails(
        userPathRef: String,
        userId: String
    ): ListenerRegistration {
         return repository.fetchUserDetails(userPathRef, userId)
             .addSnapshotListener { snapshots, exception ->
                 if (snapshots == null || exception != null){

                     return@addSnapshotListener
                 }



                 val user = snapshots.toObject(UserModel::class.java)

                 _userDetails.postValue(user!!)

             }




    }


}