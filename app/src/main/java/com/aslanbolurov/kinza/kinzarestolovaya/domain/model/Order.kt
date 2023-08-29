package com.aslanbolurov.kinza.kinzarestolovaya.domain.model

import java.util.Date

data class Order(

    var id: String,
    var dishes: Map<String,Int>,///Map of dish <Name, Cnt>
    var town: String,
    var street: String,
    var houseNumber: String,
    var flatNumber: String,
    var phoneNumber:String,
    var customerName:String,
    var title:String,
    var totalPrice:String,
    var deliveryCost:Int,
    var date:String
)
