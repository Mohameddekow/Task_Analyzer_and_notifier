package com.example.taskkeeperandanalyzer.auth.register


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val repository: RegisterRepository
) : ViewModel() {

    //listen for the auth state and registration state
    var firestoreRegState : MutableLiveData<String> = repository._firestoreRegState


    fun registerUser(
        name: String,
        email: String,
        password: String,
        usersRootRef: String
    ): Task<AuthResult> {
        return repository.registerUser(name, email, password, usersRootRef)
    }


}