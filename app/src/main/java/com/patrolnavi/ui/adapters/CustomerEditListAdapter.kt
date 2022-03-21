package com.patrolnavi.ui.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.patrolnavi.R
import com.patrolnavi.models.Customer
import com.patrolnavi.ui.activities.DetailsCustomerActivity
import com.patrolnavi.utils.Constants
import kotlinx.android.synthetic.main.item_customer_edit_list_layout.view.*

open class CustomerEditListAdapter (
    private val context: Context,
    private var list: ArrayList<Customer>
        ):RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
       return MyViewHolder(
           LayoutInflater.from(context).inflate(
               R.layout.item_customer_edit_list_layout,
               parent,
               false
           )
       )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if(holder is MyViewHolder){
            holder.itemView.tv_customer_edit_list_date.text = model.date
            holder.itemView.tv_customer_edit_list_course.text = model.course
            holder.itemView.tv_customer_edit_list_no.text = model.no.toString()
            holder.itemView.tv_customer_edit_list_first_name.text = model.firstName
            holder.itemView.tv_customer_edit_list_last_name.text = model.lastName

            holder.itemView.setOnClickListener {
                Log.i(javaClass.simpleName,"CustomerEditListAdapter groupsId : ${model.groups_id}")

                val intent = Intent(context, DetailsCustomerActivity::class.java)
                intent.putExtra(Constants.EXTRA_CUSTOMER_ID,model.customer_id)
                intent.putExtra(Constants.EXTRA_GROUPS_ID,model.groups_id)
                context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int {
       return list.size
    }

    class MyViewHolder(view: View):RecyclerView.ViewHolder(view)

}