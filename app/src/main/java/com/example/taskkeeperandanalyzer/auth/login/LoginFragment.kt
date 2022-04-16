package com.example.taskkeeperandanalyzer.auth.login

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.taskkeeperandanalyzer.R
import com.example.taskkeeperandanalyzer.databinding.FragmentLoginBinding
import com.example.taskkeeperandanalyzer.utils.showLongToast
import com.example.taskkeeperandanalyzer.utils.showShortToast
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)


        binding.tvRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }


        //log in logic
        binding.apply {
            btnLogin.setOnClickListener {

                val email = etEmailAddress.text.toString().trim()
                val password = etPassword.text.toString().trim()

                when {
                    TextUtils.isEmpty(email) && TextUtils.isEmpty(password) -> {
                        showShortToast(requireContext(), "All fields are required")
                    }

                    TextUtils.isEmpty(email) -> {
                        showShortToast(requireContext(), "email field can't be empty")
                    }

                    TextUtils.isEmpty(password) -> {
                        showShortToast(requireContext(), "password field can't be empty")
                    }
                    else -> {

                        // dismiss keyboard
                        val inputManager = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        inputManager.hideSoftInputFromWindow(binding.btnLogin.windowToken, 0)


                        //authenticating user through mvvm and navigate to home fragment after successful auth
                        loginViewModel.authenticateUser(email, password).addOnCompleteListener { task ->
                            if (task.isSuccessful){
                                //show success toast
                                showLongToast(requireContext(), "Login successful")
                                findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                            }else{
                                //show failed toast
                                showLongToast(requireContext(), "Login failed due to: ${task.exception?.message}")
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
