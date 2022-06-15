package pl.przemyslawpitus.brum.domain.service

import org.springframework.stereotype.Service
import pl.przemyslawpitus.brum.domain.entity.MileageRecord
import pl.przemyslawpitus.brum.domain.entity.NewMileageRecord
import pl.przemyslawpitus.brum.domain.entity.UserId
import pl.przemyslawpitus.brum.domain.entity.VehicleId
import pl.przemyslawpitus.brum.domain.repository.MileageRecordRepository

@Service
class MileageRecordService(
    private val mileageRecordRepository: MileageRecordRepository,
    private val vehicleOwnerValidator: VehicleOwnerValidator,
) {
    fun createMileageRecord(newMileageRecord: NewMileageRecord): MileageRecord {
        vehicleOwnerValidator.validateUserOwnVehicle(
            userId = newMileageRecord.userId,
            vehicleId = newMileageRecord.vehicleId
        )
        return mileageRecordRepository.createMileageRecord(newMileageRecord)
    }

    fun getMileageRecords(userId: UserId, vehicleId: VehicleId): List<MileageRecord> {
        vehicleOwnerValidator.validateUserOwnVehicle(userId = userId, vehicleId = vehicleId)
        return mileageRecordRepository.getMileageRecords().filter { it.vehicleId == vehicleId }
    }

}