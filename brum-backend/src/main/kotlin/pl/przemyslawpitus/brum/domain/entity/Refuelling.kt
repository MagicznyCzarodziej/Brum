package pl.przemyslawpitus.brum.domain.entity

import java.math.BigDecimal
import java.time.Instant

typealias RefuellingId = Int

data class Refuelling(
    val id: RefuellingId,
    val userId: UserId,
    val vehicleId: VehicleId,
    val timestamp: Instant,
    val fuelType: FuelType,
    val fuelLiters: Double,
    val gasStation: String,
    val currentMileage: Int?,
)

enum class FuelType {
    GAS, DIESEL, LPG,
}

data class NewRefuelling(
    val userId: UserId,
    val vehicleId: VehicleId,
    val timestamp: Instant,
    val fuelType: FuelType,
    val fuelLiters: Double,
    val gasStation: String,
    val currentMileage: Int?,
    val createExpense: Boolean,
    val expense: RefuellingExpense?,
)

data class RefuellingExpense(
    val amount: BigDecimal,
)