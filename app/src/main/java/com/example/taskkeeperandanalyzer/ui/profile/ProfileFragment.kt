package com.example.taskkeeperandanalyzer.ui.profile

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.taskkeeperandanalyzer.R
import com.example.taskkeeperandanalyzer.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment(): Fragment() {

    //auth injection
    @Inject lateinit var auth: FirebaseAuth  //field inject

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


        binding.editProfileBtn.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_editProfileFragment)
        }

        return binding.root
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.profile_menu,menu)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.logoutUser ->{
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