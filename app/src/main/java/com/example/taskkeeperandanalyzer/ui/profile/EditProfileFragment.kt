package com.example.taskkeeperandanalyzer.ui.profile

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
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
import com.example.taskkeeperandanalyzer.utils.loadImageWithGlide
import com.example.taskkeeperandanalyzer.utils.showLongToast
import com.example.taskkeeperandanalyzer.utils.showProgressDialog
import com.example.taskkeeperandanalyzer.utils.showShortToast
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



        //retrieve user details
        binding.apply {


            val userId = auth.currentUser?.uid
            if (userId != null) {
                editProfileViewModel.fetchUserDetails(USERS_ROOT_REF, userId)

            }


            editProfileViewModel.userDetails.observe(viewLifecycleOwner){  userModel ->
                etName.setText(userModel.name)
                tvEmail.text = userModel.email

                userModel.profileUrl?.let {
                    loadImageWithGlide(it, requireContext(), profileImage)
                }
            }

        }


        //profile edit
        binding.apply {

            val showProgressDialog = showProgressDialog(
                requireContext(),
                "Updating profile",
                "Update is in progress. Please wait..."
            )

            updateProfileBtn.setOnClickListener {


                val name = etName.text.toString().trim()

                when {

                    TextUtils.isEmpty(name) -> {
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

                        //check if user selected an image
                        //if no image selected update only the name
                        //otherwise update all his details

                        if (photoUploadUri == null){
                            //if no image selected update the name only
                            //check if user made any changes to his name

                            //if he made no change tel him no change made
                            //initialize  i#initialName
                             editProfileViewModel.userDetails.observe(viewLifecycleOwner){
                                 initialName = it.name.toString()
                            }
                            if (name == initialName){
                                showLongToast(requireContext(), "You have made no changes.")
                                //enable btn btn update on failure
                                binding.updateProfileBtn.isEnabled = true


                                //dismiss on failure
                                showProgressDialog.dismiss()

                                return@setOnClickListener
                            }else{
                                //otherwise update user profile name
                                if (userId != null){
                                    editProfileViewModel.updateUserProfileNameOnly(name, userId, USERS_ROOT_REF)
                                        .addOnCompleteListener { task ->
                                            if (task.isSuccessful){

                                                showShortToast(requireContext(), "profile name updated successfully")
                                                findNavController().navigate(R.id.action_editProfileFragment_to_profileFragment)

                                                //enable btn btn update on success
                                                binding.updateProfileBtn.isEnabled = true

                                                //dismiss on success
                                                showProgressDialog.dismiss()

                                            }else{

                                                //enable btn btn update on failure
                                                binding.updateProfileBtn.isEnabled = true


                                                //dismiss on failure
                                                showProgressDialog.dismiss()

                                                showLongToast(requireContext(), "${task.exception?.message}")
                                            }
                                        }
                                }
                            }

                        }else{
                            //update all user profile details

                            if (userId != null) {
                                editProfileViewModel.updateAllUserProfileDetails(
                                    name,
                                    userId,
                                    USERS_ROOT_REF,
                                    photoUploadUri,
                                    PROFILE_IMAGES_ROOT_REF
                                ).addOnCompleteListener { task ->
                                    if (task.isSuccessful){
                                        showShortToast(requireContext(), "profile updated successfully")
                                        findNavController().navigate(R.id.action_editProfileFragment_to_profileFragment)

                                        //enable btn btn update on success
                                        binding.updateProfileBtn.isEnabled = true

                                        //dismiss on success
                                        showProgressDialog.dismiss()

                                    }else{


                                        //enable btn btn update on failure
                                        binding.updateProfileBtn.isEnabled = true


                                        //dismiss on failure
                                        showProgressDialog.dismiss()

                                        showLongToast(requireContext(), "${task.exception?.message}")

                                    }
                                }

                            }


                        }

                    }
                }
            }
        }





        return binding.root
    }


    //image picking
    private fun pickImageFromGallery() {
        val imagePickerIntent = Intent(Intent.ACTION_GET_CONTENT)
        imagePickerIntent.type = "image/*"

        startActivityForResult(imagePickerIntent, PICK_IMAGE_CODE)

        // dismiss keyboard
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
//
//                showLongToast(requireContext(),"photo path is: ${ photoUri.toString()}")


                Log.d("photo", photoUri.toString())

            } else {
                showLongToast(requireContext(), "Image picking canceled")
            }
        }
    }


    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}