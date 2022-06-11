package pl.przemyslawpitus.brum.domain

import org.springframework.stereotype.Service

@Service
class VehiclesService(
    private val vehiclesRepository: VehiclesRepository,
) {
    fun getVehicles() = vehiclesRepository.getVehicles()
}