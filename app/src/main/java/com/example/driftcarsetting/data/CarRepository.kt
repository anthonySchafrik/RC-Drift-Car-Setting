package com.example.driftcarsetting.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CarRepository(private val carDao: CarDao) {
    val allCars: Flow<List<CarModel>> = carDao.getAllCars().map { entities ->
        entities.map { it.toCarModel() }
    }

    suspend fun insertCar(car: CarModel) {
        carDao.insertCar(car.toCarEntity())
    }

    suspend fun updateCar(car: CarModel) {
        carDao.updateCar(car.toCarEntity())
    }

    suspend fun deleteCar(car: CarModel) {
        carDao.deleteCar(car.toCarEntity())
    }
}
