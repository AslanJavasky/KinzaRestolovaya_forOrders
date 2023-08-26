package com.aslanbolurov.kinza.kinzarestolovaya.domain.usecase

import com.aslanbolurov.kinza.kinzarestolovaya.domain.repo.DishRepo
import javax.inject.Inject

class DeleteAllDishesFromDbUseCase @Inject constructor(
    private val repo: DishRepo
) {
    suspend operator fun invoke() = repo.deleteAllDishesFromDb()
}