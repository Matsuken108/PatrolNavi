package com.patrolnavi.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.patrolnavi.R
import com.patrolnavi.firestore.FirestoreClass
import com.patrolnavi.models.Groups
import com.patrolnavi.ui.activities.SettingCourseActivity
import com.patrolnavi.ui.activities.DetailsGroupsActivity
import com.patrolnavi.utils.Constants
import kotlinx.android.synthetic.main.item_groups_setting_list_layout.view.*

open class SettingGroupsListAdapter(
    private val context: Context,
    private var list: ArrayList<Groups>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_groups_setting_list_layout,
                parent,
                false
            )
        )
    }

    @SuppressLint
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if (holder is MyViewHolder) {
            holder.itemView.tv_groups_name_list.text = model.name

            holder.itemView.setOnClickListener {
                val intent = Intent(context, SettingCourseActivity::class.java)
                intent.putExtra(Constants.EXTRA_GROUPS_ID, model.groups_id)
                intent.putExtra(Constants.EXTRA_GROUPS_NAME, model.name)
                context.startActivity(intent)
            }
            if(model.owner == FirestoreClass().getCurrentUserID()) {
            holder.itemView.iv_groups_edit.visibility = View.VISIBLE
            holder.itemView.iv_groups_edit.setOnClickListener {
                val intent = Intent(context, DetailsGroupsActivity::class.java)
                intent.putExtra(Constants.EXTRA_GROUPS_NAME, model.name)
                intent.putExtra(Constants.EXTRA_GROUPS_PASS, model.password)
                intent.putExtra(Constants.EXTRA_GROUPS_ID, model.groups_id)
                intent.putExtra(Constants.EXTRA_GROUPS_LAT,model.groups_lat)
                intent.putExtra(Constants.EXTRA_GROUPS_LNG,model.groups_lng)
                context.startActivity(intent)
                }
            } else{
                holder.itemView.iv_groups_edit.visibility = View.GONE
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
}