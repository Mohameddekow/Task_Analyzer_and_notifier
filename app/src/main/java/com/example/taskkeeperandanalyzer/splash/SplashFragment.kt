package com.example.taskkeeperandanalyzer.splash

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.taskkeeperandanalyzer.R
import com.example.taskkeeperandanalyzer.databinding.FragmentSplashBinding

class SplashFragment : Fragment() {
    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSplashBinding.inflate(inflater, container, false)



        Handler().postDelayed(
            {
              //  if (onBoardingFinished()){
                //if user is log in go home

                    findNavController().navigate(R.id.action_splashFagment_to_homeFragment)

               // }else{

                // else go to login

                    //findNavController().navigate(R.id.action_splashFragment_to_loginFragment)

               // }
            },2000
        )


        return binding.root
    }


    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}