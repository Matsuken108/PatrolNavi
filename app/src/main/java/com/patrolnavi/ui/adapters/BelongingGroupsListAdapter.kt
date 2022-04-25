package com.patrolnavi.ui.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.patrolnavi.R
import com.patrolnavi.firestore.FirestoreClass
import com.patrolnavi.models.BelongingGroups
import com.patrolnavi.ui.activities.DeleteBelongingGroupsUserActivity
import com.patrolnavi.utils.Constants
import kotlinx.android.synthetic.main.item_belonging_groups_layout.view.*
import kotlinx.android.synthetic.main.item_edit_groups_users_list_layout.view.*

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

            if(model.groups_user_id !== FirestoreClass().getCurrentUserID()){
                holder.itemView.iv_belonging_groups_list_delete.visibility = View.VISIBLE
            holder.itemView.iv_belonging_groups_list_delete.setOnClickListener {
                val intent = Intent(context, DeleteBelongingGroupsUserActivity::class.java)
                intent.putExtra(Constants.EXTRA_GROUPS_ID, model.groups_id)
                context.startActivity(intent)
            }
            } else{
                holder.itemView.iv_belonging_groups_list_delete.visibility = View.GONE
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
}
