package pl.przemyslawpitus.brum.domain.service

import org.springframework.stereotype.Service
import pl.przemyslawpitus.brum.domain.entity.Expense
import pl.przemyslawpitus.brum.domain.entity.UserId
import pl.przemyslawpitus.brum.domain.entity.Vehicle
import pl.przemyslawpitus.brum.domain.entity.VehicleId
import pl.przemyslawpitus.brum.domain.repository.ExpenseRepository
import pl.przemyslawpitus.brum.domain.repository.VehicleRepository

@Service
class VehicleService(
    private val vehicleRepository: VehicleRepository,
    private val expenseRepository: ExpenseRepository,
) {
    fun getVehicles() = vehicleRepository.getVehicles()

    fun getVehiclesForUser(userId: Int) = vehicleRepository.getVehicles().filter { it.userId == userId }

    fun addVehicle(vehicle: Vehicle) = vehicleRepository.addVehicle(vehicle)

    fun getExpenses(userId: UserId, vehicleId: VehicleId): List<Expense> {
        validateUserOwnVehicle(userId, vehicleId)
        return expenseRepository.getExpensesByVehicleId(vehicleId)
    }

    fun addExpense(expense: Expense): Expense {
        validateUserOwnVehicle(userId = expense.userId, vehicleId = expense.vehicleId)
        return expenseRepository.saveExpense(expense)
    }

    private fun validateUserOwnVehicle(userId: Int, vehicleId: VehicleId) {
        vehicleRepository.getVehicles()
            .find { it.id == vehicleId && it.userId == userId }
            ?: throw VehicleNotFoundException(vehicleId)
    }
}

class VehicleNotFoundException(vehicleId: VehicleId) : RuntimeException("Vehicle $vehicleId not found")
