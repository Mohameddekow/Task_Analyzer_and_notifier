package com.example.taskkeeperandanalyzer.auth.password

import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.SignInMethodQueryResult
import com.google.firebase.firestore.QuerySnapshot
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class ResetPasswordViewModel @Inject constructor(
    private val repository: ResetPasswordRepository
): ViewModel() {

    fun resetPassword(
        email: String
    ): Task<Void> {
        return repository.resetPassword(email)
    }

}