package com.patrolnavi.ui.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.patrolnavi.R
import com.patrolnavi.models.BelongingGroups
import com.patrolnavi.ui.activities.EditBelongingGroupsUserActivity
import com.patrolnavi.utils.Constants
import kotlinx.android.synthetic.main.item_belonging_groups_layout.view.*

class BelongingGroupsListAdapter(
    private val context: Context,
    private var list: ArrayList<BelongingGroups>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_belonging_groups_layout,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if (holder is MyViewHolder) {
            holder.itemView.tv_belonging_groups_list_name.text = model.groups_name

            holder.itemView.iv_belonging_groups_list_edit.setOnClickListener {
                val intent = Intent(context, EditBelongingGroupsUserActivity::class.java)
                intent.putExtra(Constants.EXTRA_GROUPS_ID,model.groups_id)
                intent.putExtra(Constants.EXTRA_GROUPS_USER_NAME,model.groups_user_name)
                intent.putExtra(Constants.BELONGING_GROUPS_ID,model.belonging_groups_id)
                intent.putExtra(Constants.EXTRA_GROUPS_USER_ID,model.groups_user_id)
                context.startActivity(intent)
            }

        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
}
