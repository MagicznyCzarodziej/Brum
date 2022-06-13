package pl.przemyslawpitus.brum.domain.repository

import pl.przemyslawpitus.brum.domain.entity.Vehicle

interface VehicleRepository {
    fun getVehicles(): List<Vehicle>
    fun addVehicle(vehicle: Vehicle): Vehicle
}