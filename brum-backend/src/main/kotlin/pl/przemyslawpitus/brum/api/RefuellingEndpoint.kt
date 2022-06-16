package pl.przemyslawpitus.brum.api

import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import pl.przemyslawpitus.brum.domain.entity.FuelType
import pl.przemyslawpitus.brum.domain.entity.NewRefuelling
import pl.przemyslawpitus.brum.domain.entity.Refuelling
import pl.przemyslawpitus.brum.domain.entity.RefuellingExpense
import pl.przemyslawpitus.brum.domain.entity.UserDetails
import pl.przemyslawpitus.brum.domain.entity.UserId
import pl.przemyslawpitus.brum.domain.entity.VehicleId
import pl.przemyslawpitus.brum.domain.service.MissingRefuellingExpense
import pl.przemyslawpitus.brum.domain.service.RefuellingService
import pl.przemyslawpitus.brum.domain.service.VehicleNotFoundException
import java.math.BigDecimal
import java.time.Instant

@RestController
class RefuellingEndpoint(
    private val refuellingService: RefuellingService,
) {
    @GetMapping(path = ["/vehicles/{vehicleId}/refuellings"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getRefuellings(
        @AuthenticationPrincipal userDetails: UserDetails,
        @PathVariable vehicleId: Int,
    ): ResponseEntity<*> {
        val userId = userDetails.id
        return try {
            val response = refuellingService.getRefuellings(
                userId = userId,
                vehicleId = vehicleId,
            ).toDto()
            ResponseEntity.ok(response)
        } catch (exception: VehicleNotFoundException) {
            ResponseEntity.notFound().build<Void>()
        }
    }

    @PostMapping(path = ["/vehicles/{vehicleId}/refuellings"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun addRefuelling(
        @AuthenticationPrincipal userDetails: UserDetails,
        @RequestBody body: NewRefuellingDto,
        @PathVariable vehicleId: Int,
    ): ResponseEntity<*> {
        val userId = userDetails.id

        return try {
            val response = refuellingService
                .addRefuelling(body.toDomain(userId, vehicleId))
                .toDto()
            ResponseEntity.ok(response)
        } catch (exception: VehicleNotFoundException) {
            ResponseEntity.notFound().build<Void>()
        } catch (exception: MissingRefuellingExpense) {
            ResponseEntity.badRequest().build<Void>()
        }
    }
}

data class NewRefuellingDto(
    val timestamp: String,
    val fuelType: String,
    val fuelLiters: Double,
    val gasStation: String,
    val currentMileage: Int?,
    val createExpense: Boolean,
    val expense: RefuellingExpenseDto?,
) {
    fun toDomain(userId: UserId, vehicleId: VehicleId) = NewRefuelling(
        userId = userId,
        vehicleId = vehicleId,
        timestamp = Instant.parse(this.timestamp),
        fuelType = FuelType.valueOf(this.fuelType),
        fuelLiters = this.fuelLiters,
        gasStation = this.gasStation,
        currentMileage = this.currentMileage,
        createExpense = this.createExpense,
        expense = this.expense?.toDomain(),
    )
}

data class RefuellingExpenseDto(
    val amount: String,
) {
    fun toDomain() = RefuellingExpense(
        amount = BigDecimal(amount),
    )
}

data class RefuellingDto(
    val id: Int,
    val vehicleId: Int,
    val timestamp: String,
    val fuelType: String,
    val fuelLiters: Double,
    val gasStation: String,
    val currentMileage: Int?,
)

private fun Refuelling.toDto() = RefuellingDto(
    id = this.id,
    vehicleId = this.vehicleId,
    timestamp = this.timestamp.toString(),
    fuelType = this.fuelType.name,
    fuelLiters = this.fuelLiters,
    gasStation = this.gasStation,
    currentMileage = this.currentMileage,
)

data class RefuellingsDto(
    val refuellings: List<RefuellingDto>,
    val totalCount: Int,
)

private fun List<Refuelling>.toDto() = RefuellingsDto(
    refuellings = this.map { it.toDto() },
    totalCount = this.size,
)