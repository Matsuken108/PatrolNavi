package com.patrolnavi.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.patrolnavi.R
import com.patrolnavi.models.GroupsUsers
import com.patrolnavi.ui.activities.DetailsGroupsUsersActivity
import com.patrolnavi.utils.Constants
import kotlinx.android.synthetic.main.item_edit_groups_users_list_layout.view.*

open class EditGroupsUsersListAdapter(
    private val context: Context,
    private var list: ArrayList<GroupsUsers>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_edit_groups_users_list_layout,
                parent,
                false
            )
        )
    }

    @SuppressLint
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if (holder is MyViewHolder) {
            holder.itemView.tv_edit_groups_user_name_list.text = model.groups_user_name

            holder.itemView.iv_edit_groups_user_list_edit.setOnClickListener {
                val intent = Intent(context, DetailsGroupsUsersActivity::class.java)

                Log.i(javaClass.simpleName,"user_name : ${model.groups_user_name}")

                intent.putExtra(Constants.EXTRA_GROUPS_ID, model.groups_id)
                intent.putExtra(Constants.EXTRA_GROUPS_USER_NAME, model.groups_user_name)
                intent.putExtra(Constants.BELONGING_GROUPS_ID, model.belonging_groups_id)
                intent.putExtra(Constants.EXTRA_GROUPS_USER_ID, model.groups_user_id)
                context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
}