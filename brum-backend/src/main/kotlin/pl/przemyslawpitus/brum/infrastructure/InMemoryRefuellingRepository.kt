package pl.przemyslawpitus.brum.infrastructure

import org.springframework.stereotype.Repository
import pl.przemyslawpitus.brum.domain.entity.FuelType
import pl.przemyslawpitus.brum.domain.entity.NewRefuelling
import pl.przemyslawpitus.brum.domain.entity.Refuelling
import pl.przemyslawpitus.brum.domain.entity.RefuellingId
import pl.przemyslawpitus.brum.domain.repository.RefuellingRepository
import java.time.Instant

@Repository
class InMemoryRefuellingRepository : RefuellingRepository {
    private val refuellings: MutableList<Refuelling> = mutableListOf(
        Refuelling(
            id = 1,
            userId = 1,
            vehicleId = 1,
            timestamp = Instant.now(),
            fuelType = FuelType.GAS,
            fuelLiters = 45.12,
            currentMileage = 123_456,
            gasStation = "BP",
        )
    )

    override fun getRefuellings(): List<Refuelling> {
        return refuellings
    }

    override fun createRefuelling(newRefuelling: NewRefuelling): Refuelling {
        val refuelling = Refuelling(
            id = getNextId(),
            userId = newRefuelling.userId,
            vehicleId = newRefuelling.vehicleId,
            timestamp = newRefuelling.timestamp,
            fuelType = newRefuelling.fuelType,
            fuelLiters = newRefuelling.fuelLiters,
            gasStation = newRefuelling.gasStation,
            currentMileage = newRefuelling.currentMileage,
        )
        refuellings.add(refuelling)
        return refuelling
    }

    private fun getNextId(): RefuellingId {
        return refuellings.size
    }
}