package pl.przemyslawpitus.brum.api

import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import pl.przemyslawpitus.brum.domain.entity.MileageRecord
import pl.przemyslawpitus.brum.domain.entity.UserDetails
import pl.przemyslawpitus.brum.domain.service.MileageRecordService
import pl.przemyslawpitus.brum.domain.service.VehicleNotFoundException

@RestController
class MileageRecordEndpoint(
    private val mileageRecordService: MileageRecordService,
) {
    @GetMapping(path = ["/vehicles/{vehicleId}/mileage-records"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getRefuellings(
        @AuthenticationPrincipal userDetails: UserDetails,
        @PathVariable vehicleId: Int,
    ): ResponseEntity<*> {
        val userId = userDetails.id
        return try {
            val response = mileageRecordService.getMileageRecords(
                userId = userId,
                vehicleId = vehicleId,
            ).toDto()
            ResponseEntity.ok(response)
        } catch (exception: VehicleNotFoundException) {
            ResponseEntity.notFound().build<Void>()
        }
    }
}

data class MileageRecordsDto(
    val mileageRecords: List<MileageRecordDto>,
    val totalCount: Int,
)

data class MileageRecordDto(
    val id: Int,
    val userId: Int,
    val vehicleId: Int,
    val timestamp: String,
    val mileage: Int,
)

private fun List<MileageRecord>.toDto() = MileageRecordsDto(
    mileageRecords = this.map { it.toDto() },
    totalCount = this.size,
)

private fun MileageRecord.toDto() = MileageRecordDto(
    id = this.id,
    userId = this.userId,
    vehicleId = this.vehicleId,
    timestamp = this.timestamp.toString(),
    mileage = this.mileage
)