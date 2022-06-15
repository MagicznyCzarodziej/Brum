package pl.przemyslawpitus.brum.domain.repository

import pl.przemyslawpitus.brum.domain.entity.NewVehicle
import pl.przemyslawpitus.brum.domain.entity.Vehicle

interface VehicleRepository {
    fun createVehicle(newVehicle: NewVehicle): Vehicle
    fun getVehicles(): List<Vehicle>
}