package pl.przemyslawpitus.brum.domain.service

import org.springframework.stereotype.Service
import pl.przemyslawpitus.brum.domain.entity.NewMileageRecord
import pl.przemyslawpitus.brum.domain.entity.NewRefuelling
import pl.przemyslawpitus.brum.domain.entity.Refuelling
import pl.przemyslawpitus.brum.domain.entity.UserId
import pl.przemyslawpitus.brum.domain.entity.VehicleId
import pl.przemyslawpitus.brum.domain.repository.RefuellingRepository

@Service
class RefuellingService(
    private val vehicleOwnerValidator: VehicleOwnerValidator,
    private val refuellingRepository: RefuellingRepository,
    private val mileageRecordService: MileageRecordService,
) {

    fun addRefuelling(newRefuelling: NewRefuelling): Refuelling {
        vehicleOwnerValidator.validateUserOwnVehicle(userId = newRefuelling.userId, vehicleId = newRefuelling.vehicleId)
        mileageRecordService.createMileageRecord(
            NewMileageRecord(
                userId = newRefuelling.userId,
                vehicleId = newRefuelling.vehicleId,
                timestamp = newRefuelling.timestamp,
                mileage = newRefuelling.currentMileage,
            )
        )

        return refuellingRepository.createRefuelling(newRefuelling)
    }

    fun getRefuellings(userId: UserId, vehicleId: VehicleId): List<Refuelling> {
        vehicleOwnerValidator.validateUserOwnVehicle(userId = userId, vehicleId = vehicleId)
        return refuellingRepository.getRefuellings().filter { it.vehicleId == vehicleId }
    }
}