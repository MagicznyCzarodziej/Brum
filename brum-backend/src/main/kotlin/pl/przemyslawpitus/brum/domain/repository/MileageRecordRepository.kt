package pl.przemyslawpitus.brum.domain.repository

import pl.przemyslawpitus.brum.domain.entity.MileageRecord
import pl.przemyslawpitus.brum.domain.entity.NewMileageRecord

interface MileageRecordRepository {
    fun createMileageRecord(newMileageRecord: NewMileageRecord): MileageRecord
    fun getMileageRecords(): List<MileageRecord>
}