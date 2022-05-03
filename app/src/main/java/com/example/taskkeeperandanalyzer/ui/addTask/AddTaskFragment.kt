package com.example.taskkeeperandanalyzer.ui.addTask

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.RadioButton
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.taskkeeperandanalyzer.R
import com.example.taskkeeperandanalyzer.constants.TASK_ROOT_REF
import com.example.taskkeeperandanalyzer.databinding.FragmentAddTaskBinding
import com.example.taskkeeperandanalyzer.utils.Resource
import com.example.taskkeeperandanalyzer.utils.isInternetAvailable
import com.example.taskkeeperandanalyzer.utils.showLongToast
import com.example.taskkeeperandanalyzer.utils.showShortToast
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class AddTaskFragment : Fragment() {
    private var _binding: FragmentAddTaskBinding? = null
    private val binding get() = _binding!!

    private val addTaskViewModel by viewModels<AddTaskViewModel>()

    @Inject
    lateinit var auth: FirebaseAuth


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAddTaskBinding.inflate(inflater, container, false)





        //log in logic
        binding.apply {
            btnAddTask.setOnClickListener {

                val userId = auth.currentUser?.uid
                val title = etTitle.text.toString().trim()
                val desc = etDesc.text.toString().trim()
                val remainderTime = getTime()

                if ( taskTypeRadioGroup.checkedRadioButtonId == -1 ) {
                        //nothing selected
                        showLongToast(requireContext(),"please select the task type")
                        return@setOnClickListener
                }

                //getting title of the selected radio btn
                val radioButton: RadioButton = requireActivity().findViewById(taskTypeRadioGroup.checkedRadioButtonId)
                val taskType = radioButton.text.toString()

                when {

                    TextUtils.isEmpty(title) && TextUtils.isEmpty(desc) -> {
                        showShortToast(requireContext(), "All fields are required")
                    }

                    TextUtils.isEmpty(title) -> {
                        showShortToast(requireContext(), "title field can't be empty")
                    }

                    TextUtils.isEmpty(desc) -> {
                        showShortToast(requireContext(), "description field can't be empty")
                    }
                    else -> {

                        //disable btn login
                        binding.btnAddTask.alpha = 0.6F
                        binding.btnAddTask.isEnabled = false

                        // dismiss keyboard
                        val inputManager =
                            context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        inputManager.hideSoftInputFromWindow(binding.btnAddTask.windowToken, 0)

                        if (userId != null) {

                            if (isInternetAvailable(requireContext())){
                                addTaskViewModel.addTaskToFireStore(userId, TASK_ROOT_REF,title, desc, remainderTime, taskType)

                                addTaskViewModel.addingTaskState.observe(viewLifecycleOwner){ task ->

                                    progressBar.isVisible = task is Resource.Loading

                                    when(task){
                                        is Resource.Success ->{
                                            showSuccessChanges("Task added successfully")
                                        }
                                        is Resource.Error ->{
                                            showErrorChanges(task.error?.message!!)
                                        }
                                        else -> {
                                            //do nothing
                                        }
                                    }

                                }
                            }else{
                                showErrorChanges("No internet connection")
                            }

                        }


                    }
                }
            }
        }


        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun getTime(): Long {
        val year = binding.datePicker.year
        val month = binding.datePicker.month
        val day = binding.datePicker.dayOfMonth
        val hour = binding.timePicker.hour
        val minute = binding.timePicker.minute

        val calender = Calendar.getInstance()
        calender.set(year,month, day, hour, minute)
        return calender.timeInMillis
    }



    private fun showSuccessChanges(message: String){
        showShortToast(requireContext(), message)
        binding.btnAddTask.isEnabled = true
        binding.btnAddTask.alpha = 1F
        findNavController().navigate(R.id.action_addTaskFragment_to_homeFragment)
    }

    private fun showErrorChanges(message: String){
        showLongToast(requireContext(),message)
        binding.btnAddTask.isEnabled = true
        binding.btnAddTask.alpha = 1F
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}