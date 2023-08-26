package com.aslanbolurov.kinza.kinzarestolovaya.presentation.orderFragment

import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aslanbolurov.kinza.kinzarestolovaya.App
import com.aslanbolurov.kinza.kinzarestolovaya.R
import com.aslanbolurov.kinza.kinzarestolovaya.data.utils.DataUtils
import com.aslanbolurov.kinza.kinzarestolovaya.domain.interactor.OrderDaoInteractor
import com.aslanbolurov.kinza.kinzarestolovaya.domain.model.DishItem
import com.aslanbolurov.kinza.kinzarestolovaya.domain.model.Order
import com.aslanbolurov.kinza.kinzarestolovaya.domain.usecase.GetAllDishesFromDbUseCase
import com.aslanbolurov.kinza.kinzarestolovaya.domain.usecase.GetTotalSumOfDishesByOrderIdUseCase
import com.aslanbolurov.kinza.kinzarestolovaya.domain.usecase.SaveOrderToDbUseCase
import com.aslanbolurov.kinza.kinzarestolovaya.domain.usecase.SendOrderToFirebaseUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID
import javax.inject.Inject

class OrderViewModel @Inject constructor(
    private val getTotalSumUseCase: GetTotalSumOfDishesByOrderIdUseCase,
    private val orderDaoInteractor: OrderDaoInteractor,
    private val getAllDishesFromDbUseCase: GetAllDishesFromDbUseCase,
    private val sendOrderToFirebaseUseCase: SendOrderToFirebaseUseCase,
    private val saveOrderToDbUseCase: SaveOrderToDbUseCase
) : ViewModel() {

    private val _cart_sum = MutableStateFlow(0)
    val cart_sum = _cart_sum.asStateFlow()

    private val _order = MutableStateFlow(getEmptyOrder())
    val order = _order.asStateFlow()

    val dishesFromDb = getAllDishesFromDbUseCase()
    lateinit var DB_DISHES_LIST: List<DishItem>

    init {
        viewModelScope.launch(Dispatchers.IO) {
            dishesFromDb.collectLatest {
                DB_DISHES_LIST = it
            }
        }
    }

    fun refreshTotalSum() {
        viewModelScope.launch(Dispatchers.IO) {
            _cart_sum.value = getTotalSumUseCase()
        }
    }


    fun getAdapterForTownSpinner() =
        ArrayAdapter(
            App.INSTANCE,
            android.R.layout.simple_spinner_dropdown_item,
            DataUtils.getAllTowns()
        )

    fun calculateDeliveryCost(selectedTown: String): Int {
        _order.value.deliveryCost = DataUtils.getDeliveryCost(selectedTown, _cart_sum.value)
        return _order.value.deliveryCost
    }


    fun setName(name: String) {
        _order.value.customerName = name
    }

    fun setPhoneNumber(number: String) {
        _order.value.phoneNumber = number
    }

    fun setTitle(title: String) {
        _order.value.title = title
    }

    fun setTown(town: String) {
        _order.value.town = town
    }

    fun setStreet(street: String) {
        _order.value.street = street
    }

    fun setHouseNum(house: String) {
        _order.value.houseNumber = house
    }

    fun setFlatNum(flat: String) {
        _order.value.flatNumber = flat
    }

    fun setDeliveryCost(cost: Int) {
        _order.value.deliveryCost = cost
    }

    fun sendOrderToFirebaseDb() {
        calcuclateTotalCost()
        calculateDate()
        saveOrderToRoomDb()
        saveAllDishesToOrder()
        sendOrderToFirebaseUseCase(_order.value)
        clearOrderFlow()
        Toast.makeText(App.INSTANCE, R.string.order_success, Toast.LENGTH_LONG).show()
    }

    private fun calculateDate() {
        _order.value.date=
            SimpleDateFormat(
                "yyyy.MM.dd HH:mm:ss",
                Locale.getDefault()
            ).format(Date())

    }

    private fun saveOrderToRoomDb() {
        viewModelScope.launch(Dispatchers.IO) {
            saveOrderToDbUseCase(_order.value)
        }
    }

    private fun calcuclateTotalCost() {
        _order.value.totalPrice = (_cart_sum.value + _order.value.deliveryCost).toString()
    }

    private fun saveAllDishesToOrder() {
        val maps = mutableListOf<MutableMap<String, Int>>()
        DB_DISHES_LIST.forEachIndexed { index, element ->
            maps.add(
                index, mutableMapOf(deleteUnsupportSymbols(element.name) to element.cnt))
        }
        _order.value.dishes = maps
    }

    private fun deleteUnsupportSymbols(str:String):String{
        return str
            .replace("/","")
            .replace(".","")
            .replace("#","")
            .replace("$","")
            .replace("[","")
            .replace("]","")
    }

    private fun clearOrderFlow() {
        _cart_sum.value = 0
        _order.value = getEmptyOrder()
    }

    private fun getEmptyOrder() = Order(
        UUID.randomUUID().toString(),
        emptyList(),
        "",
        "",
        "",
        "",
        "",
        "",
        "Любимой доченке, приятного аппетита))",
        "",
        0,
        ""
        )

}



