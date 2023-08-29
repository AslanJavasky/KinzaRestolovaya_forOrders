package com.aslanjavasky.kinzarestolovaya.kinzakitchen.presentation

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aslanbolurov.kinza.kinzarestolovaya.databinding.OrderItemBinding
import com.aslanbolurov.kinza.kinzarestolovaya.domain.model.Order
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class FirebaseOrdersAdapter(
    private val options: FirebaseRecyclerOptions<String>
) : FirebaseRecyclerAdapter<String, FirebaseOrdersAdapter.OrderViewHolder>(options) {

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): OrderViewHolder {

        val binding= OrderItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        return OrderViewHolder(binding)

    }

    override fun onBindViewHolder(
        holder: OrderViewHolder, position: Int, model: String
    ) {
        holder.bind(model)
    }

    inner class OrderViewHolder(private val binding: OrderItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String) {
            val order= Gson().fromJson(item, Order::class.java)
            binding.tvDate.text=order.date
            binding.tvAddress.text="${order.town}, ${order.street},${order.houseNumber}, кв. ${order.flatNumber}"
            binding.tvPhone.text=order.phoneNumber
            binding.tvPrice.text="${order.totalPrice} руб."
            binding.tvTitle.text=order.title
            binding.tvDishes.text=order.getDishes()

        }
    }
}