package com.example.taskkeeperandanalyzer.ui.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.taskkeeperandanalyzer.databinding.RecyclerviewRowItemBinding
import com.example.taskkeeperandanalyzer.model.TaskModel
import com.example.taskkeeperandanalyzer.utils.MyDiffUtil
import java.text.SimpleDateFormat

class HomeRecyclerViewAdapter (
    private val clickListener: ItemClickListener
): RecyclerView.Adapter<HomeRecyclerViewAdapter.RestaurantViewHolder>(){

    private var oldTasksList = emptyList<TaskModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantViewHolder {
        val binding: RecyclerviewRowItemBinding = RecyclerviewRowItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false)

        return RestaurantViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int) {
        val currentRestaurantItem = oldTasksList[position]
        holder.onBind(currentRestaurantItem, clickListener)
    }

    override fun getItemCount(): Int {
        return oldTasksList.size
    }



    inner class RestaurantViewHolder(
        private val binding: RecyclerviewRowItemBinding
    ): RecyclerView.ViewHolder(binding.root){

        @SuppressLint("SimpleDateFormat")
        fun onBind(taskItem: TaskModel, action: ItemClickListener){
            binding.apply {

                tvTitle.text = taskItem.title
                tvDesc.text = taskItem.desc
                taskTypeTag.text = taskItem.taskType

                val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy hh:mm")
                val dateString = simpleDateFormat.format(taskItem.remainderTime)

                tvRemainderTime.text = dateString

            }

            binding.root.setOnClickListener {
                action.onItemClicked(it, taskItem, adapterPosition)
            }
        }

    }

    fun setData(newRestaurants: List<TaskModel> ){
        val diffUtil = MyDiffUtil(oldTasksList, newRestaurants)
        val diffUtilResult = DiffUtil.calculateDiff(diffUtil)
        oldTasksList = newRestaurants
        diffUtilResult.dispatchUpdatesTo(this)
//        RestaurantComparator()

    }

    interface ItemClickListener{
        fun onItemClicked(view: View, taskListItem: TaskModel, position: Int)
    }



    //comparison
    class RestaurantComparator(): DiffUtil.ItemCallback<TaskModel>(){
        override fun areItemsTheSame(oldItem: TaskModel, newItem: TaskModel) =
            oldItem.title == newItem.title

        override fun areContentsTheSame(oldItem: TaskModel, newItem: TaskModel) =
            oldItem == newItem

    }
}