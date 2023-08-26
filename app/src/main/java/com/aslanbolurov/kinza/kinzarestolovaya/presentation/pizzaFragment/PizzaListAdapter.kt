package com.aslanbolurov.kinza.kinzarestolovaya.presentation.pizzaFragment

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aslanbolurov.kinza.kinzarestolovaya.App
import com.aslanbolurov.kinza.kinzarestolovaya.R
import com.aslanbolurov.kinza.kinzarestolovaya.databinding.DishItemBinding
import com.aslanbolurov.kinza.kinzarestolovaya.domain.model.DishItem
import com.aslanbolurov.kinza.kinzarestolovaya.domain.model.DishTypesConst
import com.aslanbolurov.kinza.kinzarestolovaya.presentation.dishesFragment.DishesViewModel
import com.aslanbolurov.kinza.kinzarestolovaya.presentation.viewModelDb.ViewModelDb
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class PizzaListAdapter(
    private val viewModelDb: ViewModelDb,
    private val lifecycleScope: LifecycleCoroutineScope,
)
    : ListAdapter<DishItem, DishesViewHolder>(callback) {

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): DishesViewHolder {

        val binding=
            DishItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return DishesViewHolder(binding)

    }

    override fun onBindViewHolder(
        holder: DishesViewHolder, position: Int
    ) {
        val dishItem = getItem(position)
        holder.binding.tvName.text = dishItem.name
        holder.binding.tvPrice.text = dishItem.price

        lifecycleScope.launch {
            viewModelDb.dishesFromDb_Flow.collectLatest {
                setIconDependentlyStateSelected(dishItem, holder)
                holder.binding.btnToCart.text = viewModelDb.getCntOfDish(dishItem)
                dishItem.isSelected=viewModelDb.getSelectedState(dishItem)

            }
        }
        holder.binding.btnToCart.setOnClickListener {
            if (!dishItem.isSelected) {
                viewModelDb.saveItem(dishItem, DishTypesConst.TYPE_DESSERT)

            } else {
                viewModelDb.removeItem(dishItem)
            }
        }
        holder.binding.plus.setOnClickListener {
            viewModelDb.incrementCntOfDishItem(dishItem)
        }
        holder.binding.minus.setOnClickListener {
            viewModelDb.decrementCntOfDishItem(dishItem)
        }

    }

    private fun setIconDependentlyStateSelected(
        dishItem: DishItem, holder: DishesViewHolder
    ) {
        val isSelected = viewModelDb.getSelectedState(dishItem)
        val icon = if (isSelected) R.drawable.cart_icon else R.drawable.remove_shopping_cart_24
        holder.binding.btnToCart.icon = ResourcesCompat.getDrawable(
            App.INSTANCE.resources,
            icon,
            null
        )
        setVisiblePlusMinusBtns(isSelected, holder)
        if (!isSelected) holder.binding.btnToCart.text = ""

    }

    private fun setVisiblePlusMinusBtns(value: Boolean,holder: DishesViewHolder) {
        holder.binding.minus.isVisible = value
        holder.binding.plus.isVisible = value

    }
}

class DishesViewHolder(val binding: DishItemBinding) : RecyclerView.ViewHolder(binding.root)

val callback = object : DiffUtil.ItemCallback<DishItem>() {
    override fun areItemsTheSame(oldItem: DishItem, newItem: DishItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: DishItem, newItem: DishItem): Boolean {
        return oldItem == newItem
    }

}


