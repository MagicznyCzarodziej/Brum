package pl.przemyslawpitus.brum.domain

import pl.przemyslawpitus.brum.domain.entity.Vehicle

interface VehiclesRepository {
    fun getVehicles(): List<Vehicle>
}