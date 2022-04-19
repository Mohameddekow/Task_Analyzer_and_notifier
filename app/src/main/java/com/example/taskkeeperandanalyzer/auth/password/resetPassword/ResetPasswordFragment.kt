package com.example.taskkeeperandanalyzer.auth.password.resetPassword

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
import com.example.taskkeeperandanalyzer.databinding.FragmentResetPasswordBinding
import com.example.taskkeeperandanalyzer.utils.showLongToast
import com.example.taskkeeperandanalyzer.utils.showResetPasswordAlertDialog
import com.example.taskkeeperandanalyzer.utils.showShortToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ResetPasswordFragment : Fragment(){
    private var _binding: FragmentResetPasswordBinding? = null
    private val binding get() = _binding!!


    private val passwordViewModel by viewModels<PasswordViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentResetPasswordBinding.inflate(inflater, container, false)


        binding.apply {


            btnSubmit.setOnClickListener {

                val email = etEmailAddress.text.toString().trim()

                when {
                    TextUtils.isEmpty(email) -> {
                        showShortToast(requireContext(), "email field can't be empty")
                    }

                    else -> {

                        //disable btn login
                        binding.btnSubmit.alpha = 0.6F
                        binding.progressBar.isVisible = true
                        binding.btnSubmit.isEnabled = false


                        // dismiss keyboard
                        val inputManager =
                            context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        inputManager.hideSoftInputFromWindow(binding.btnSubmit.windowToken, 0)


                        passwordViewModel.resetPassword(email).addOnCompleteListener { task ->
                            if (task.isSuccessful){

                                showResetPasswordAlertDialog(requireContext())


                                //re enable if success
                                binding.btnSubmit.alpha = 01F
                                binding.progressBar.isVisible = false
                                binding.btnSubmit.isEnabled = true

                                showShortToast(requireContext(), "email sent successfully")

                                findNavController().navigate(R.id.action_resetPasswordFragment_to_loginFragment)

                            }else{

                                //re enable if failure
                                binding.btnSubmit.alpha = 01F
                                binding.progressBar.isVisible = false
                                binding.btnSubmit.isEnabled = true

                                showLongToast(requireContext(), "Email wasn't because: ${task.exception?.message}")
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