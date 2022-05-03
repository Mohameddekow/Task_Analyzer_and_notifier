package com.example.taskkeeperandanalyzer.ui.profile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.taskkeeperandanalyzer.R
import com.example.taskkeeperandanalyzer.constants.USERS_ROOT_REF
import com.example.taskkeeperandanalyzer.databinding.FragmentProfileBinding
import com.example.taskkeeperandanalyzer.utils.Resource
import com.example.taskkeeperandanalyzer.utils.loadImageWithGlide
import com.example.taskkeeperandanalyzer.utils.showLongToast
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment(): Fragment() {

    //auth injection
    @Inject
    lateinit var auth: FirebaseAuth  //field inject

    private val profileViewModel by viewModels<ProfileViewModel>()


    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentProfileBinding.inflate(inflater, container, false)


        //set up profile menu items
        setHasOptionsMenu(true)

        binding.changePasswordTv.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_currentPasswordFragment)
        }

        binding.editProfileBtn.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_editProfileFragment)
        }




        binding.apply {

            val userId = auth.currentUser?.uid
            if (userId != null) {
                profileViewModel.fetchUserDetails(USERS_ROOT_REF, userId)
            }

            profileViewModel.userDetails.observe(viewLifecycleOwner){  result ->

                progressBar.isVisible = result is Resource.Loading  //show only when state is *Loading

                when(result){
                    is Resource.Success -> {
                        tvName.text = result.data?.name
                        tvEmail.text = result.data?.email

                        result.data?.profileUrl?.let {
                            loadImageWithGlide(it, requireContext(), profileImage)
                        }

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


        return binding.root
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.profile_menu, menu)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.logoutUser -> {
                //logout a user
                //initialize
//                auth = FirebaseAuth.getInstance()
                auth.signOut()

                //back login frag
                findNavController().navigate(R.id.action_profileFragment_to_loginFragment)

            }
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}