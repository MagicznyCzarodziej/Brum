package pl.przemyslawpitus.brum.domain.service

import org.springframework.stereotype.Service
import pl.przemyslawpitus.brum.domain.entity.NewVehicle
import pl.przemyslawpitus.brum.domain.entity.VehicleId
import pl.przemyslawpitus.brum.domain.repository.VehicleRepository

@Service
class VehicleService(
    private val vehicleRepository: VehicleRepository,
) {
    fun getVehiclesByUserId(userId: Int) = vehicleRepository.getVehicles().filter { it.userId == userId }

    fun addVehicle(newVehicle: NewVehicle) = vehicleRepository.createVehicle(newVehicle)

}

class VehicleNotFoundException(vehicleId: VehicleId) : RuntimeException("Vehicle $vehicleId not found")
