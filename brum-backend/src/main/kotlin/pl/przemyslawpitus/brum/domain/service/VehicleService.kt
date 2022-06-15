package pl.przemyslawpitus.brum.domain.service

import org.springframework.stereotype.Service
import pl.przemyslawpitus.brum.domain.entity.Expense
import pl.przemyslawpitus.brum.domain.entity.NewExpense
import pl.przemyslawpitus.brum.domain.entity.NewRefuelling
import pl.przemyslawpitus.brum.domain.entity.NewVehicle
import pl.przemyslawpitus.brum.domain.entity.Refuelling
import pl.przemyslawpitus.brum.domain.entity.UserId
import pl.przemyslawpitus.brum.domain.entity.VehicleId
import pl.przemyslawpitus.brum.domain.repository.ExpenseRepository
import pl.przemyslawpitus.brum.domain.repository.RefuellingRepository
import pl.przemyslawpitus.brum.domain.repository.VehicleRepository

@Service
class VehicleService(
    private val vehicleRepository: VehicleRepository,
    private val expenseRepository: ExpenseRepository,
    private val refuellingRepository: RefuellingRepository,
) {
    fun getVehicles() = vehicleRepository.getVehicles()

    fun getVehiclesByUserId(userId: Int) = vehicleRepository.getVehicles().filter { it.userId == userId }

    fun addVehicle(newVehicle: NewVehicle) = vehicleRepository.createVehicle(newVehicle)

    fun getExpenses(userId: UserId, vehicleId: VehicleId): List<Expense> {
        validateUserOwnVehicle(userId = userId, vehicleId = vehicleId)
        return expenseRepository.getExpenses().filter { it.vehicleId == vehicleId }
    }

    fun addExpense(newExpense: NewExpense): Expense {
        validateUserOwnVehicle(userId = newExpense.userId, vehicleId = newExpense.vehicleId)
        return expenseRepository.createExpense(newExpense)
    }

    fun addRefuelling(newRefuelling: NewRefuelling): Refuelling {
        validateUserOwnVehicle(userId = newRefuelling.userId, vehicleId = newRefuelling.vehicleId)
        return refuellingRepository.createRefuelling(newRefuelling)
    }

    fun getRefuellings(userId: UserId, vehicleId: VehicleId) : List<Refuelling> {
        validateUserOwnVehicle(userId = userId, vehicleId = vehicleId)
        return refuellingRepository.getRefuellings().filter { it.vehicleId == vehicleId }
    }

    private fun validateUserOwnVehicle(userId: Int, vehicleId: VehicleId) {
        vehicleRepository.getVehicles()
            .find { it.id == vehicleId && it.userId == userId }
            ?: throw VehicleNotFoundException(vehicleId)
    }
}

class VehicleNotFoundException(vehicleId: VehicleId) : RuntimeException("Vehicle $vehicleId not found")
