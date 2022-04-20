package com.example.taskkeeperandanalyzer.ui.profile

import android.net.Uri
import com.example.taskkeeperandanalyzer.model.UserModel
import com.example.taskkeeperandanalyzer.model.UserProfileModel
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.storage.FirebaseStorage
import javax.inject.Inject


class ProfileRepository @Inject constructor(
    private val firebaseStorage: FirebaseStorage,
    private val auth: FirebaseAuth,
    private val fireStoreDb: FirebaseFirestore
) {




    //update user details
    fun updateUserProfile(
        name: String,
        userId: String,
        usersPathRef: String,
        photoUri: Uri?,
        profileImagesRootRef: String
    ): Task<Void>? {

        val email = auth.currentUser?.email
        val photoStoragePathRef = firebaseStorage.reference.child("${profileImagesRootRef}/${userId}")

        return photoUri?.let {
            photoStoragePathRef.putFile(it)
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
        }
    }


    //fetch user details
    fun fetchUserDetails(
        userPathRef: String,
        userId: String
    ): DocumentReference {

        return fireStoreDb.collection(userPathRef).document(userId)

    }
}