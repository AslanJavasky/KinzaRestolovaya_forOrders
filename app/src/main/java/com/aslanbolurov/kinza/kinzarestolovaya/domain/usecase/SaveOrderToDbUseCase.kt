package com.aslanbolurov.kinza.kinzarestolovaya.domain.usecase

import com.aslanbolurov.kinza.kinzarestolovaya.domain.model.Order
import com.aslanbolurov.kinza.kinzarestolovaya.domain.repo.OrderRepo
import javax.inject.Inject

class SaveOrderToDbUseCase @Inject constructor(
    private val repo:OrderRepo
) {
    suspend operator fun invoke(order: Order){
        repo.saveOrderToRoom(order)
    }
}