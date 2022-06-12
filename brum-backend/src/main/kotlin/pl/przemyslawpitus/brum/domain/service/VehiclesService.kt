package pl.przemyslawpitus.brum.domain.service

import org.springframework.stereotype.Service
import pl.przemyslawpitus.brum.domain.repository.VehiclesRepository

@Service
class VehiclesService(
    private val vehiclesRepository: VehiclesRepository,
) {
    fun getVehicles() = vehiclesRepository.getVehicles()
}