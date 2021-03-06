package com.example.todoapp

import android.app.AlertDialog
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.data.Todo
import com.example.todoapp.data.TodoViewModel
import com.example.todoapp.databinding.ListItemBinding

class TodoAdapter(private val viewModel: TodoViewModel) :
    ListAdapter<Todo, TodoAdapter.ViewHolder>(TodoDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemBinding.inflate(inflater)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.todoText.text = viewModel.todos.value!![position].task

        /* Remove */
        viewHolder.delBtn.setOnClickListener {
            viewModel.removeTodo(getItem(viewHolder.adapterPosition))
        }

        /* Edit */
        viewHolder.editBtn.setOnClickListener {
            val context = viewHolder.itemView.context
            val inflater = LayoutInflater.from(context)
            val view = inflater.inflate(R.layout.edit_item, null)

            val prevText = getItem(position).task
            val editText = view.findViewById<TextView>(R.id.editTextItem)
            editText.text = prevText


            /* Dialog */
            var alertDialog = AlertDialog.Builder(context)
            alertDialog.setTitle("Edit Item")
                .setView(view)
                .setPositiveButton("Update", DialogInterface.OnClickListener { dialog, id ->
                    val editedText = editText.text.toString()
                    viewModel.updateTodo(viewHolder.adapterPosition, editedText)
                    viewHolder.todoText.text = editedText
                })
                .setNegativeButton("Cancel", DialogInterface.OnClickListener { dialogInterface, i ->

                })
            alertDialog.show()
        }
    }

    override fun getItemCount() = viewModel.todos.value!!.size

    class ViewHolder(binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val todoText = binding.textViewItem
        val delBtn = binding.btnDelete
        val editBtn = binding.btnEdit
    }
}


/*Diff Util*/
class TodoDiffCallback : DiffUtil.ItemCallback<Todo>() {
    override fun areItemsTheSame(oldItem: Todo, newItem: Todo): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Todo, newItem: Todo): Boolean {
        return oldItem.equals(newItem)
    }
}