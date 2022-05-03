package com.example.taskkeeperandanalyzer.ui.profile

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.taskkeeperandanalyzer.R
import com.example.taskkeeperandanalyzer.constants.PICK_IMAGE_CODE
import com.example.taskkeeperandanalyzer.constants.PROFILE_IMAGES_ROOT_REF
import com.example.taskkeeperandanalyzer.constants.USERS_ROOT_REF
import com.example.taskkeeperandanalyzer.databinding.FragmentEditProfileBinding
import com.example.taskkeeperandanalyzer.utils.*
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class EditProfileFragment : Fragment() {
    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!


    @Inject
    lateinit var auth: FirebaseAuth

    private val editProfileViewModel by viewModels<ProfileViewModel>()

    private var photoUri: Uri? = null

    private lateinit var initialName: String

    private lateinit var showProgressDialog: ProgressDialog
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)

        binding.profileImage.setOnClickListener {
            //pick image
            pickImageFromGallery()
        }
        binding.addPhoto.setOnClickListener {
            //pick image
            pickImageFromGallery()

        }

        //progressDialog to show on updating profile
        showProgressDialog = showProgressDialog(
            requireContext(),
            "Updating profile",
            "Update is in progress. Please wait..."
        )




        //retrieve user details
        binding.apply {

            val userId = auth.currentUser?.uid
            if (userId != null) {
                editProfileViewModel.fetchUserDetails(USERS_ROOT_REF, userId)
            }

            editProfileViewModel.userDetails.observe(viewLifecycleOwner){  result ->

                progressBar.isVisible = result is Resource.Loading  //show only when state is *Loading

                when(result){
                    is Resource.Success -> {
                        etName.setText(result.data?.name)
                        tvEmail.text = result.data?.email

                        result.data?.profileUrl?.let {
                            loadImageWithGlide(it, requireContext(), profileImage)
                        }


                        //initialize the initial name on success
                        initialName = result.data?.name.toString()

                    }

                    is Resource.Error -> {
                        showLongToast(requireContext(), result.error.toString())
                    }
                    else -> {
                        //show nothing
                    }
                }

            }
        }


        //profile edit
        binding.apply {

            updateProfileBtn.setOnClickListener {


                val newName = etName.text.toString().trim()

                when {

                    TextUtils.isEmpty(newName) -> {
                        showShortToast(requireContext(), "name field can't be empty")
                    }

                    else -> {

                        //show dialog
                        showProgressDialog.show()

                        //disable btn login
                        binding.updateProfileBtn.isEnabled = false


                        // dismiss keyboard
                        val inputManager =
                            context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        inputManager.hideSoftInputFromWindow(
                            binding.updateProfileBtn.windowToken,
                            0
                        )


                        val userId = auth.currentUser?.uid

                        val photoUploadUri = photoUri as Uri?


                        //@TODO when
                        ////////@TODO profileUri == null and initialName == newName // **make no change
                        ////////@TODO profileUri == null and initialName != newName // **update  name only

                        ////////@TODO profileUri != null and initialName != newName   // **updateAllProfileDetails
                        ////////@TODO profileUri != null and initialName == newName   // **updateAllProfileDetails


                        ////@TODO all this task should only happen when the user is not null **AND  have **internet connection**

                        if (userId != null){

                            ////@TODO user should  have **internet connection first**
                                if (isInternetAvailable(requireContext())){

                                    if (photoUploadUri == null && initialName == newName) {

                                        showErrorChanges("You have made no changes.")

                                        return@setOnClickListener
                                    }else if (photoUploadUri == null && initialName != newName) {
                                        //update profile name only
                                        editProfileViewModel.updateUserProfileNameOnly(newName, userId, USERS_ROOT_REF)

                                        editProfileViewModel.userNameUpdatingState.observe(viewLifecycleOwner){ task ->

                                            progressBar.isVisible = task is Resource.Loading

                                            when(task){
                                                is Resource.Success ->{
                                                    showSuccessChanges("name updated successfully")
                                                }

                                                is Resource.Error ->{
                                                    showErrorChanges(task.error?.message.toString())
                                                }
                                                else -> {
                                                    //do nothing
                                                }
                                            }

                                        }

                                    }else if (
                                        (photoUploadUri != null && initialName != newName)
                                        || (photoUploadUri != null && initialName == newName)
                                    ){
                                        //both cases update all profile details
                                        editProfileViewModel.updateAllUserProfileDetails(
                                            newName,
                                            userId,
                                            USERS_ROOT_REF,
                                            photoUploadUri,
                                            PROFILE_IMAGES_ROOT_REF
                                        )
                                        editProfileViewModel.updatingAllProfileDetailsState.observe(viewLifecycleOwner){ task ->
                                            progressBar.isVisible = task is Resource.Loading

                                            when(task){
                                                is Resource.Success ->{
                                                    showSuccessChanges("profile updated successfully")
                                                }

                                                is Resource.Error ->{
                                                   showErrorChanges(task.error?.message.toString())
                                                }
                                                else -> {
                                                    //do nothing
                                                }
                                            }

                                        }
                                    }

                                }else{
                                    showErrorChanges("No internet connection")
                                }
                        }


                    }
                }
            }
        }





        return binding.root
    }


    //image picking fun
    private fun pickImageFromGallery() {
        val imagePickerIntent = Intent(Intent.ACTION_GET_CONTENT)
        imagePickerIntent.type = "image/*"

        startActivityForResult(imagePickerIntent, PICK_IMAGE_CODE)

        //dismiss keyboard
        val inputManager =
            context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(binding.updateProfileBtn.windowToken, 0)

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_CODE) {
            if (resultCode == Activity.RESULT_OK) {

                photoUri = data?.data
                val imageView = binding.profileImage
                imageView.setImageURI(photoUri)

//                showLongToast(requireContext(),"photo path is: ${ photoUri.toString()}")

                Log.d("photo", photoUri.toString())

            } else {
                showLongToast(requireContext(), "Image picking canceled")
            }
        }
    }

    private fun showSuccessChanges(message: String){
        showShortToast(requireContext(), message)
        showProgressDialog.dismiss()
        binding.updateProfileBtn.isEnabled = true
        findNavController().navigate(R.id.action_editProfileFragment_to_profileFragment)
    }

    private fun showErrorChanges(message: String){
        showLongToast(requireContext(),message)
        showProgressDialog.dismiss()
        binding.updateProfileBtn.isEnabled = true
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}