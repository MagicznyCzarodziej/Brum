package pl.przemyslawpitus.brum.api

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import pl.przemyslawpitus.brum.domain.VehiclesService
import pl.przemyslawpitus.brum.domain.entity.Vehicle
import java.util.*

@RestController
class VehicleEndpoint(
    val vehiclesService: VehiclesService
) {

    @GetMapping(path = ["/vehicles"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getVehicles(): VehiclesDto {
        return vehiclesService.getVehicles().toDto()
    }
}

private fun List<Vehicle>.toDto() = VehiclesDto(
    vehicles = this.map { VehicleDto(it.id, it.name) },
    totalCount = this.size,
)

data class VehiclesDto(
    val vehicles: List<VehicleDto>,
    val totalCount: Int,
)

data class VehicleDto(
    val id: UUID,
    val name: String,
)