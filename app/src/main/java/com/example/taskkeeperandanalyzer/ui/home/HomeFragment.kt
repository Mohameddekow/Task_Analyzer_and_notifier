package com.example.taskkeeperandanalyzer.ui.home

import android.os.Bundle
import android.view.*
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.taskkeeperandanalyzer.R
import com.example.taskkeeperandanalyzer.constants.TASK_ROOT_REF
import com.example.taskkeeperandanalyzer.databinding.FragmentHomeBinding
import com.example.taskkeeperandanalyzer.model.TaskModel
import com.example.taskkeeperandanalyzer.utils.Resource
import com.example.taskkeeperandanalyzer.utils.showLongToast
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class HomeFragment : Fragment(), HomeRecyclerViewAdapter.ItemClickListener {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val homeViewModel by viewModels<HomeViewModel>()

    @Inject
    lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        //setting up the home menu items
        setHasOptionsMenu(true)

        val myAdapter = HomeRecyclerViewAdapter(this)

        binding.homeFab.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_addTaskFragment)
        }

        binding.apply {

            val userId = auth.currentUser?.uid
            if (userId != null) {
                homeViewModel.getAllTasks(userId, TASK_ROOT_REF)
            }

            homeViewModel.fetchingTasksState.observe(viewLifecycleOwner){  result ->

               // progressBar.isVisible = result is Resource.Loading  //show only when state is *Loading


                when(result){
                    is Resource.Success -> {

                        homeRecyclerView.apply {
                            adapter = myAdapter
                            layoutManager = LinearLayoutManager(requireContext())
                        }
                        result.data?.let { myAdapter.setData(it) }

                        shimmerFrameLayout.stopShimmer()
                        shimmerFrameLayout.visibility = View.GONE
                    }

                    is Resource.Loading ->{
                        shimmerFrameLayout.startShimmer()
                        shimmerFrameLayout.visibility = View.VISIBLE
                    }
                    is Resource.Error -> {
                        showLongToast(requireContext(), result.error.toString())
                        shimmerFrameLayout.stopShimmer()
                        shimmerFrameLayout.visibility = View.GONE
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

    override fun onItemClicked(view: View, taskListItem: TaskModel, position: Int) {
        //do nothing
    }

}