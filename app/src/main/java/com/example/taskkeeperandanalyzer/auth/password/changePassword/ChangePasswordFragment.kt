package com.example.taskkeeperandanalyzer.auth.password.changePassword

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.taskkeeperandanalyzer.R
import com.example.taskkeeperandanalyzer.auth.password.PasswordViewModel
import com.example.taskkeeperandanalyzer.databinding.FragmentChangePasswordBinding
import com.example.taskkeeperandanalyzer.databinding.FragmentEditProfileBinding
import com.example.taskkeeperandanalyzer.utils.showLongToast
import com.example.taskkeeperandanalyzer.utils.showShortToast
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ChangePasswordFragment : Fragment() {
    private var _binding: FragmentChangePasswordBinding? = null
    private val binding get() = _binding!!

    private val passwordViewModel by viewModels<PasswordViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentChangePasswordBinding.inflate(inflater, container, false)



        //log in logic
        binding.apply {
            btnUpdate.setOnClickListener {

                val newPassword = etPassword.text.toString().trim()
                val confirmNewPassword = etConfirmPassword.text.toString().trim()


                when {
                    TextUtils.isEmpty(confirmNewPassword) && TextUtils.isEmpty(newPassword) -> {
                        showShortToast(requireContext(), "All fields are required")
                    }

                    TextUtils.isEmpty(confirmNewPassword) -> {
                        showShortToast(requireContext(), "confirm password field can't be empty")
                    }

                    TextUtils.isEmpty(newPassword) -> {
                        showShortToast(requireContext(), "password field can't be empty")
                    }
                    else -> {

                        //disable btn btn update
                        binding.btnUpdate.alpha = 0.6F
                        binding.progressBar.isVisible = true
                        binding.btnUpdate.isEnabled = false


                        // dismiss keyboard
                        val inputManager =
                            context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        inputManager.hideSoftInputFromWindow(binding.btnUpdate.windowToken, 0)

                        if (newPassword.length < 6){
                            showLongToast(requireContext(),"password must be atleast 6 characters")
                            return@setOnClickListener
                        }

                        if (newPassword == confirmNewPassword){

                            passwordViewModel.changePassword(newPassword)?.addOnCompleteListener { task ->
                                if (task.isSuccessful){
                                    showLongToast(requireContext(),"Password updated successfully")

                                    findNavController().navigate(R.id.action_changePasswordFragment_to_profileFragment)

                                    //disable btn btn update on success
                                    binding.btnUpdate.alpha = 1F
                                    binding.progressBar.isVisible = false
                                    binding.btnUpdate.isEnabled = true

                                }else{
                                    showLongToast(requireContext(), "password update failed due: ${task.exception?.message}")

                                    //disable btn btn update on failure
                                    binding.btnUpdate.alpha = 1F
                                    binding.progressBar.isVisible = false
                                    binding.btnUpdate.isEnabled = true
                                }
                            }

                        }else{
                            showLongToast(requireContext(), "password didn't match")
                            //disable btn btn update on failure
                            binding.btnUpdate.alpha = 1F
                            binding.progressBar.isVisible = false
                            binding.btnUpdate.isEnabled = true
                        }

                    }
                }
            }
        }


        return binding.root
    }


    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}