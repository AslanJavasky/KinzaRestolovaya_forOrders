package com.aslanbolurov.kinza.kinzarestolovaya.domain.model

data class Order(

    var id: String = "",
    var dishes: Map<String, Int> = emptyMap(),///Map of dish <Name, Cnt>
    var town: String = "",
    var street: String = "",
    var houseNumber: String = "",
    var flatNumber: String = "",
    var phoneNumber: String = "",
    var customerName: String = "",
    var title: String = "",
    var totalPrice: String = "",
    var deliveryCost: Int = 0,
    var date: String = ""
){
    fun getDishes():String=dishes.entries.joinToString(separator = "\n") {
        "${it.key} : ${it.value} шт."
    }
}
