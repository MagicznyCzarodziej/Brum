package pl.przemyslawpitus.brum.domain.service

import org.springframework.stereotype.Component
import pl.przemyslawpitus.brum.domain.entity.VehicleId
import pl.przemyslawpitus.brum.domain.repository.VehicleRepository

@Component
class VehicleOwnerValidator(
    private val vehicleRepository: VehicleRepository,
) {

    fun validateUserOwnVehicle(userId: Int, vehicleId: VehicleId) {
        vehicleRepository.getVehicles()
            .find { it.id == vehicleId && it.userId == userId }
            ?: throw VehicleNotFoundException(vehicleId)
    }
}