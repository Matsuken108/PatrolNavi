package com.patrolnavi.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.patrolnavi.R
import com.patrolnavi.models.Customer
import kotlinx.android.synthetic.main.item_customer_list_layout.view.*

open class CustomerListAdapter (
    private val context : Context,
    private var list:ArrayList<Customer>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_customer_list_layout,
                parent,
                false
            )
        )
    }

    @SuppressLint
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if(holder is MyViewHolder){
            holder.itemView.tv_customer_list_no.text = model.no.toString()
            holder.itemView.tv_customer_list_first_name.text = model.firstName
            holder.itemView.tv_customer_list_last_name.text = model.lastName
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class MyViewHolder(view: View): RecyclerView.ViewHolder(view)
}