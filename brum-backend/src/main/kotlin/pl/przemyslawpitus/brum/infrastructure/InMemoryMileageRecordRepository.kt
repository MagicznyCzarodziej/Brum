package pl.przemyslawpitus.brum.infrastructure

import org.springframework.stereotype.Repository
import pl.przemyslawpitus.brum.domain.entity.MileageRecord
import pl.przemyslawpitus.brum.domain.entity.NewMileageRecord
import pl.przemyslawpitus.brum.domain.repository.MileageRecordRepository
import java.time.Instant

@Repository
class InMemoryMileageRecordRepository : MileageRecordRepository {
    private val mileageRecords: MutableList<MileageRecord> = mutableListOf(
        MileageRecord(
            id = 1,
            userId = 1,
            vehicleId = 1,
            timestamp = Instant.now(),
            mileage = 111_111,
        )
    )

    override fun createMileageRecord(newMileageRecord: NewMileageRecord): MileageRecord {
        val mileageRecord = MileageRecord(
            id = getNextId(),
            userId = newMileageRecord.userId,
            vehicleId = newMileageRecord.vehicleId,
            timestamp = newMileageRecord.timestamp,
            mileage = newMileageRecord.mileage,
        )

        mileageRecords.add(mileageRecord)
        return mileageRecord
    }

    override fun getMileageRecords(): List<MileageRecord> {
        return mileageRecords
    }

    private fun getNextId() = mileageRecords.size+1
}