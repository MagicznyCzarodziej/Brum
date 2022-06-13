package pl.przemyslawpitus.brum.infrastructure

import org.springframework.stereotype.Repository
import pl.przemyslawpitus.brum.domain.repository.VehicleRepository
import pl.przemyslawpitus.brum.domain.entity.Vehicle
import pl.przemyslawpitus.brum.domain.entity.VehicleType
import java.time.Year

@Repository
class InMemoryVehicleRepository: VehicleRepository {
    private val vehicles: MutableList<Vehicle> = mutableListOf(
        Vehicle(
            id = 1,
            userId = 1,
            name = "moje",
            type = VehicleType.CAR,
            brand = "Volvo",
            model = "V50",
            productionYear = Year.of(2008),
            mileage = 123_456
        ),
        Vehicle(
            id = 2,
            userId = 2,
            name = "niemoje",
            type = VehicleType.MOTORCYCLE,
            brand = "Honda",
            model = "Virago",
            productionYear = Year.of(1998),
            mileage = 30_000
        )
    )

    override fun getVehicles(): List<Vehicle> {
        return vehicles
    }

    override fun addVehicle(vehicle: Vehicle): Vehicle {
        vehicles.add(vehicle)
        return vehicle
    }
}