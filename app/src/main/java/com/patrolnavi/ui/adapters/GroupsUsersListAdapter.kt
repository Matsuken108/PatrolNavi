package com.patrolnavi.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.patrolnavi.R
import com.patrolnavi.models.GroupsUsers
import kotlinx.android.synthetic.main.item_groups_users_list_layout.view.*

open class GroupsUsersListAdapter(
    private val context: Context,
    private var list: ArrayList<GroupsUsers>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_groups_users_list_layout,
                parent,
                false
            )
        )
    }

    @SuppressLint
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if (holder is MyViewHolder) {
            holder.itemView.tv_groups_user_name_list.text = model.groups_user_name
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
}