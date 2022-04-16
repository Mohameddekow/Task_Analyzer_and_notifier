package com.example.taskkeeperandanalyzer.ui.home

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.taskkeeperandanalyzer.R
import com.example.taskkeeperandanalyzer.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint


class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        //setting up the home menu items
        setHasOptionsMenu(true)

        binding.homeFab.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_addTaskFragment)
        }


        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home_menu,menu)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.profileFragment ->{
                //profile frag
               findNavController().navigate(R.id.action_homeFragment_to_profileFragment)

            }
            R.id.settingsFragment ->{
                //setting frag
               findNavController().navigate(R.id.action_homeFragment_to_settingsFragment)

            }
        }
        return super.onOptionsItemSelected(item)
    }



    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}