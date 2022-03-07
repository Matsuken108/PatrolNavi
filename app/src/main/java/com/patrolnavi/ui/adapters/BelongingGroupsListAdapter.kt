package com.patrolnavi.ui.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.patrolnavi.R
import com.patrolnavi.models.BelongingGroups
import com.patrolnavi.ui.activities.DetailsCustomerActivity
import com.patrolnavi.ui.activities.EditGroupsUsersActivity
import com.patrolnavi.utils.Constants
import kotlinx.android.synthetic.main.item_belonging_groups_layout.view.*
import kotlinx.android.synthetic.main.item_customer_edit_list_layout.view.*

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
            holder.itemView.tv_belonging_groups_list.text = model.groups_name

            holder.itemView.iv_belonging_groups_edit.setOnClickListener {
                val intent = Intent(context, EditGroupsUsersActivity::class.java)
                intent.putExtra(Constants.EXTRA_GROUPS_ID,model.groups_id)
                intent.putExtra(Constants.EXTRA_GROUPS_NAME,model.groups_name)
                context.startActivity(intent)
            }

        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
}
