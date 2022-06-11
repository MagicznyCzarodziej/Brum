package pl.przemyslawpitus.brum.infrastructure

import org.springframework.stereotype.Repository
import pl.przemyslawpitus.brum.domain.VehiclesRepository
import pl.przemyslawpitus.brum.domain.entity.Vehicle
import java.util.*

@Repository
class InMemoryVehiclesRepository(): VehiclesRepository {
    override fun getVehicles(): List<Vehicle> {
        return listOf(
            Vehicle(
                id = UUID.randomUUID(),
                name = "somename"
            )
        )
    }
}