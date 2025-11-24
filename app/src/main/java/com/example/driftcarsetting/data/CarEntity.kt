package com.example.driftcarsetting.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cars")
data class CarEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val frontCamberDegreeLeft: Double,
    val frontCamberDegreeRight: Double,
    val frontCamberLengthLeft: Double,
    val frontCamberLengthRight: Double,
    val frontTowDegreeLeft: Double,
    val frontTowDegreeRight: Double,
    val frontTowLengthLeft: Double,
    val frontTowLengthRight: Double,
    val frontShockName: String,
    val frontShockLength: String,
    val frontShockPreload: Double,
    val frontRimOffset: Double,
    val rearCamberDegreeLeft: Double,
    val rearCamberDegreeRight: Double,
    val rearCamberLengthLeft: Double,
    val rearCamberLengthRight: Double,
    val rearTowDegreeLeft: Double,
    val rearTowDegreeRight: Double,
    val rearTowLengthLeft: Double,
    val rearTowLengthRight: Double,
    val rearShockName: String,
    val rearShockLength: String,
    val rearShockPreload: Double,
    val rearRimOffset: Double
)

fun CarEntity.toCarModel() = CarModel(
    id = id,
    name = name,
    frontCamberDegreeLeft = frontCamberDegreeLeft.toString(),
    frontCamberDegreeRight = frontCamberDegreeRight.toString(),
    frontCamberLengthLeft = frontCamberLengthLeft.toString(),
    frontCamberLengthRight = frontCamberLengthRight.toString(),
    frontTowDegreeLeft = frontTowDegreeLeft.toString(),
    frontTowDegreeRight = frontTowDegreeRight.toString(),
    frontTowLengthLeft = frontTowLengthLeft.toString(),
    frontTowLengthRight = frontTowLengthRight.toString(),
    frontShockName = frontShockName,
    frontShockLength = frontShockLength,
    frontShockPreload = frontShockPreload.toString(),
    frontRimOffset = frontRimOffset.toString(),
    rearCamberDegreeLeft = rearCamberDegreeLeft.toString(),
    rearCamberDegreeRight = rearCamberDegreeRight.toString(),
    rearCamberLengthLeft = rearCamberLengthLeft.toString(),
    rearCamberLengthRight = rearCamberLengthRight.toString(),
    rearTowDegreeLeft = rearTowDegreeLeft.toString(),
    rearTowDegreeRight = rearTowDegreeRight.toString(),
    rearTowLengthLeft = rearTowLengthLeft.toString(),
    rearTowLengthRight = rearTowLengthRight.toString(),
    rearShockName = rearShockName,
    rearShockLength = rearShockLength,
    rearShockPreload = rearShockPreload.toString(),
    rearRimOffset = rearRimOffset.toString()
)

fun CarModel.toCarEntity() = CarEntity(
    id = id,
    name = name,
    frontCamberDegreeLeft = frontCamberDegreeLeft.toDoubleOrNull() ?: 0.0,
    frontCamberDegreeRight = frontCamberDegreeRight.toDoubleOrNull() ?: 0.0,
    frontCamberLengthLeft = frontCamberLengthLeft.toDoubleOrNull() ?: 0.0,
    frontCamberLengthRight = frontCamberLengthRight.toDoubleOrNull() ?: 0.0,
    frontTowDegreeLeft = frontTowDegreeLeft.toDoubleOrNull() ?: 0.0,
    frontTowDegreeRight = frontTowDegreeRight.toDoubleOrNull() ?: 0.0,
    frontTowLengthLeft = frontTowLengthLeft.toDoubleOrNull() ?: 0.0,
    frontTowLengthRight = frontTowLengthRight.toDoubleOrNull() ?: 0.0,
    frontShockName = frontShockName,
    frontShockLength = frontShockLength,
    frontShockPreload = frontShockPreload.toDoubleOrNull() ?: 0.0,
    frontRimOffset = frontRimOffset.toDoubleOrNull() ?: 0.0,
    rearCamberDegreeLeft = rearCamberDegreeLeft.toDoubleOrNull() ?: 0.0,
    rearCamberDegreeRight = rearCamberDegreeRight.toDoubleOrNull() ?: 0.0,
    rearCamberLengthLeft = rearCamberLengthLeft.toDoubleOrNull() ?: 0.0,
    rearCamberLengthRight = rearCamberLengthRight.toDoubleOrNull() ?: 0.0,
    rearTowDegreeLeft = rearTowDegreeLeft.toDoubleOrNull() ?: 0.0,
    rearTowDegreeRight = rearTowDegreeRight.toDoubleOrNull() ?: 0.0,
    rearTowLengthLeft = rearTowLengthLeft.toDoubleOrNull() ?: 0.0,
    rearTowLengthRight = rearTowLengthRight.toDoubleOrNull() ?: 0.0,
    rearShockName = rearShockName,
    rearShockLength = rearShockLength,
    rearShockPreload = rearShockPreload.toDoubleOrNull() ?: 0.0,
    rearRimOffset = rearRimOffset.toDoubleOrNull() ?: 0.0
)
