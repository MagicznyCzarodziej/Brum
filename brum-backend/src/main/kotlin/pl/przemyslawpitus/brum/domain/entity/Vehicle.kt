package pl.przemyslawpitus.brum.domain.entity

import java.time.Year

typealias VehicleId = Int

data class Vehicle (
    val id: VehicleId,
    val userId: UserId,
    val name: String,
    val type: VehicleType,
    val brand: String,
    val model: String,
    val productionYear: Year,
    val mileage: Int,
)

enum class VehicleType {
    CAR, MOTORCYCLE,
}

data class NewVehicle (
    val userId: UserId,
    val name: String,
    val type: VehicleType,
    val brand: String,
    val model: String,
    val productionYear: Year,
    val mileage: Int,
)
