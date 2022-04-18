package com.example.taskkeeperandanalyzer.auth.login

import android.app.ProgressDialog
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
import com.example.taskkeeperandanalyzer.utils.showAlertDialog
import com.example.taskkeeperandanalyzer.utils.showLongToast
import com.example.taskkeeperandanalyzer.utils.showProgressDialog
import com.example.taskkeeperandanalyzer.utils.showShortToast
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!


    //the dependencies
    private val loginViewModel: LoginViewModel by viewModels()
    @Inject
    lateinit var auth: FirebaseAuth


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

                val progressDialog = showProgressDialog(
                    requireContext(),
                    "Logging in",
                    "Authentication is loading, please wait..."
                )



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

                        //show the progressDialog on loading
                        progressDialog.show()


                        // dismiss keyboard
                        val inputManager =
                            context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        inputManager.hideSoftInputFromWindow(binding.btnLogin.windowToken, 0)


                        //authenticating user through mvvm and navigate to home fragment after successful auth
                        loginViewModel.authenticateUser(email, password)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {

                                    //check if the user verified his email only then log him  in
                                    val currentUser = auth.currentUser!!
                                    if (currentUser.isEmailVerified) {
                                        //show success toast
                                        showLongToast(requireContext(), "Login successful")

                                        //dismiss the progressDialog after a successes or failure
                                        progressDialog.dismiss()

                                        findNavController().navigate(R.id.action_loginFragment_to_homeFragment)

                                    } else {
                                        //dismiss the progressDialog after a successes or failure
                                        progressDialog.dismiss()
                                        showAlertDialog(requireContext())

                                        //empty the text inputs
                                        binding.etEmailAddress.text = null
                                        binding.etPassword.text = null

                                        showLongToast(
                                            requireContext(),
                                            "email verification required"
                                        )
                                    }

                                } else {

                                    //dismiss the progressDialog after a successes or failure
                                    progressDialog.dismiss()

                                    //show failed toast
                                    showLongToast(
                                        requireContext(),
                                        "Login failed due to: ${task.exception?.message}"
                                    )

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
