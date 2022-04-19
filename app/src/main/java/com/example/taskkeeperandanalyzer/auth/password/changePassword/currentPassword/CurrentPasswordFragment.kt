package com.example.taskkeeperandanalyzer.auth.password.changePassword.currentPassword

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
import com.example.taskkeeperandanalyzer.databinding.FragmentCurrentPasswordBinding
import com.example.taskkeeperandanalyzer.utils.showLongToast
import com.example.taskkeeperandanalyzer.utils.showShortToast
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CurrentPasswordFragment :Fragment() {
    private var _binding: FragmentCurrentPasswordBinding? = null
    private val binding get() = _binding!!


    private val passwordViewModel by viewModels<PasswordViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCurrentPasswordBinding.inflate(inflater, container, false)


        //re authenticate user
        binding.apply {
            btnAuthenticate.setOnClickListener {


                val password = etPassword.text.toString().trim()

                when {
                    TextUtils.isEmpty(password) -> {
                        showShortToast(requireContext(), "password field can't be empty")
                    }
                    else -> {

                        //disable btn auth
                        binding.btnAuthenticate.alpha = 0.6F
                        binding.progressBar.isVisible = true
                        binding.btnAuthenticate.isEnabled = false


                        // dismiss keyboard
                        val inputManager =
                            context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        inputManager.hideSoftInputFromWindow(binding.btnAuthenticate.windowToken, 0)

                        passwordViewModel.reAuthenticateUser(password).addOnCompleteListener { task ->

                            if (task.isSuccessful){

                                //re enable btn auth on success
                                binding.btnAuthenticate.alpha = 1F
                                binding.progressBar.isVisible = false
                                binding.btnAuthenticate.isEnabled = true

                                findNavController().navigate(R.id.action_currentPasswordFragment_to_changePasswordFragment)

                            }else{

                                //re enable btn auth on failure
                                binding.btnAuthenticate.alpha = 1F
                                binding.progressBar.isVisible = false
                                binding.btnAuthenticate.isEnabled = true

                                showLongToast(requireContext(), "Please provide correct credentials.")
                            }

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