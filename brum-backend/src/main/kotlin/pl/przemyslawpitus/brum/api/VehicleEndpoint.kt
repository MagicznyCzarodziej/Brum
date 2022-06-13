package pl.przemyslawpitus.brum.api

import org.springframework.http.MediaType
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import pl.przemyslawpitus.brum.domain.entity.UserDetails
import pl.przemyslawpitus.brum.domain.entity.UserId
import pl.przemyslawpitus.brum.domain.service.VehicleService
import pl.przemyslawpitus.brum.domain.entity.Vehicle
import pl.przemyslawpitus.brum.domain.entity.VehicleType
import java.time.Year
import java.util.*

@RestController
class VehicleEndpoint(
    val vehicleService: VehicleService
) {

    @GetMapping(path = ["/vehicles"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getMyVehicles(
        @AuthenticationPrincipal userDetails: UserDetails,
    ): VehiclesDto {
        val userId = userDetails.id

        return vehicleService.getVehiclesForUser(userId).toDto()
    }

    @PostMapping(path = ["/vehicles"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun addMyVehicle(
        @AuthenticationPrincipal userDetails: UserDetails,
        @RequestBody body: VehicleDto,
    ): VehicleDto {
        val userId = userDetails.id

        return vehicleService
            .addVehicle(body.toDomain(userId))
            .toDto()
    }
}

private fun List<Vehicle>.toDto() = VehiclesDto(
    vehicles = this.map { it.toDto() },
    totalCount = this.size,
)

data class VehiclesDto(
    val vehicles: List<VehicleDto>,
    val totalCount: Int,
)

private fun Vehicle.toDto() = VehicleDto(
    name = this.name,
    type = this.type.name,
    brand = this.brand,
    model = this.model,
    productionYear = this.productionYear.value,
    mileage = this.mileage,
)

data class VehicleDto(
    val name: String,
    val type: String,
    val brand: String,
    val model: String,
    val productionYear: Int,
    val mileage: Int,
) {
    fun toDomain(userId: UserId) = Vehicle(
        id = Random().nextInt(),
        userId = userId,
        name = this.name,
        type = VehicleType.valueOf(this.type),
        brand = this.brand,
        model = this.model,
        productionYear = Year.of(this.productionYear),
        mileage = this.mileage,
    )
}
