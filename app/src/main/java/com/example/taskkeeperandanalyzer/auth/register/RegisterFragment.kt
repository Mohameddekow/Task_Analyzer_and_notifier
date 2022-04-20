package com.example.taskkeeperandanalyzer.auth.register

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.taskkeeperandanalyzer.R
import com.example.taskkeeperandanalyzer.constants.USERS_ROOT_REF
import com.example.taskkeeperandanalyzer.databinding.FragmentRegisterBinding
import com.example.taskkeeperandanalyzer.utils.showAlertDialog
import com.example.taskkeeperandanalyzer.utils.showLongToast
import com.example.taskkeeperandanalyzer.utils.showProgressDialog
import com.example.taskkeeperandanalyzer.utils.showShortToast
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class RegisterFragment() : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!


//    //use field inject
//    @Inject
//    lateinit var auth: FirebaseAuth //

    private val registerViewModel by viewModels<RegisterViewModel>()

//    private val registerViewModel: RegisterViewModel by viewModels() // u can do this as well


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)

        binding.tvLogin.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }

        binding.apply {
            btnRegister.setOnClickListener {
                val name = etName.text.toString().trim()
                val email = etEmailAddress.text.toString().trim()
                val password = etPassword.text.toString().trim()
                val confirmPassword = etConfirmPassword.text.toString().trim()



                val progressDialog = showProgressDialog(
                    requireContext(),
                    "Registration",
                    "Registration is loading, please wait..."
                )


                when {
                    TextUtils.isEmpty(name)
                            && TextUtils.isEmpty(email)
                            && TextUtils.isEmpty(password)
                            && TextUtils.isEmpty(confirmPassword) -> {

                        showShortToast(requireContext(), "All fields are required")
                    }

                    TextUtils.isEmpty(name) -> {
                        showShortToast(requireContext(), "name field can't be empty")
                    }

                    TextUtils.isEmpty(email) -> {
                        showShortToast(requireContext(), "email field can't be empty")
                    }

                    TextUtils.isEmpty(password) -> {
                        showShortToast(requireContext(), "password field can't be empty")
                    }

                    TextUtils.isEmpty(confirmPassword) -> {
                        showShortToast(requireContext(), "confirm password field can't be empty")
                    }
                    else -> {

                        if (password == confirmPassword) {

                            //show the progressDialog on loading
                            progressDialog.show()


                            // dismiss keyboard
                            val inputManager = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                            inputManager.hideSoftInputFromWindow(binding.btnRegister.windowToken, 0)


                            //register user and after a successful reg go to home frag
                            val profileUrl = "" //at first the profile path is null
                            registerViewModel.registerUser(name, email, password, USERS_ROOT_REF, profileUrl).addOnCompleteListener { task ->
                                if (task.isSuccessful){
                                    showLongToast(requireContext(), "Registration successful")

                                    //dismiss the progressDialog after a successes or failure
                                    progressDialog.dismiss()

                                    showAlertDialog(requireContext())


                                    //go to login frag and allow only the users who have verified their email
                                    findNavController().navigate(R.id.action_registerFragment_to_loginFragment)


                                }else{

                                    //dismiss the progressDialog after a successes or failure
                                    progressDialog.dismiss()

                                    //empty the text inputs
                                    binding.etName.text = null
                                    binding.etEmailAddress.text = null
                                    binding.etPassword.text = null
                                    binding.etConfirmPassword.text = null


                                    showLongToast(requireContext(), "Registration failed due to: ${task.exception?.message}")

                                }
                            }


                            //show fireStore reg state
                            registerViewModel.firestoreRegState.observe(
                                viewLifecycleOwner) { message ->
                                    showLongToast(requireContext(), message)

                                    Log.d("Firestore Reg State::", message.toString())
                                }


                        } else {

                            showShortToast(requireContext(), "the passwords aren't the same")


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