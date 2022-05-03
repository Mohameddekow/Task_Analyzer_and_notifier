package com.example.taskkeeperandanalyzer.ui.profile

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskkeeperandanalyzer.model.UserModel
import com.example.taskkeeperandanalyzer.utils.Resource
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.auth.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.Exception


@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val  repository: ProfileRepository
): ViewModel() {

    private val _userDetails: MutableLiveData<Resource<UserModel>> = MutableLiveData()
    val userDetails: LiveData<Resource<UserModel>> get() = _userDetails

    private val _userNameUpdatingState: MutableLiveData<Resource<Void?>> = MutableLiveData()
    val userNameUpdatingState: LiveData<Resource<Void?>> get() = _userNameUpdatingState

    private val _updatingAllProfileDetailsState: MutableLiveData<Resource<Void?>> = MutableLiveData()
    val updatingAllProfileDetailsState: LiveData<Resource<Void?>> get() = _updatingAllProfileDetailsState


    //fetch user details
    fun fetchUserDetails(
        userPathRef: String,
        userId: String
    ){
        viewModelScope.launch (){

            val result =  repository.fetchUserDetails(userPathRef, userId)

            try {
                result.collect {
                    _userDetails.postValue(it)
                }

            }catch (e: Exception){
                // Resource.Error(e, null)
                _userDetails.postValue(Resource.Error(e, null))
            }

        }
    }




    //update user profile name only
    fun updateUserProfileNameOnly(
        name: String,
        userId: String,
        usersPathRef: String
    ){
        viewModelScope.launch (Dispatchers.IO){
            val result = repository.updateUserProfileNameOnly(name, userId, usersPathRef)

            try {
                result.collect {
                    _userNameUpdatingState.postValue(it)
                }
            }catch (e: Exception){
                _userNameUpdatingState.postValue(Resource.Error(e))
            }

        }
    }


    //update user profile
    fun updateAllUserProfileDetails(
        name: String,
        userId: String,
        usersPathRef: String,
        photoUri: Uri,
        profileImagesRootRef: String
    ){
        viewModelScope.launch(Dispatchers.IO){
            val result = repository.updateAllUserProfileDetails(
                name,
                userId,
                usersPathRef,
                photoUri,
                profileImagesRootRef)

            try {
                result.collect {
                    _updatingAllProfileDetailsState.postValue(it)
                }
            }catch (e: Exception){
                _updatingAllProfileDetailsState.postValue(Resource.Error(e))
            }
        }

    }

}
