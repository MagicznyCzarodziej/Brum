package pl.przemyslawpitus.brum.domain.entity

import java.time.Instant

typealias MileageRecordId = Int

data class MileageRecord(
    val id: MileageRecordId,
    val userId: UserId,
    val vehicleId: VehicleId,
    val timestamp: Instant,
    val mileage: Int,
)

data class NewMileageRecord(
    val userId: UserId,
    val vehicleId: VehicleId,
    val timestamp: Instant,
    val mileage: Int,
)