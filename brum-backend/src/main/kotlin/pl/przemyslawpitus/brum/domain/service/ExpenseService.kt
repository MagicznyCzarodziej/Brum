package pl.przemyslawpitus.brum.domain.service

import org.springframework.stereotype.Service
import pl.przemyslawpitus.brum.domain.entity.Expense
import pl.przemyslawpitus.brum.domain.entity.NewExpense
import pl.przemyslawpitus.brum.domain.entity.UserId
import pl.przemyslawpitus.brum.domain.entity.VehicleId
import pl.przemyslawpitus.brum.domain.repository.ExpenseRepository

@Service
class ExpenseService(
    private val vehicleOwnerValidator: VehicleOwnerValidator,
    private val expenseRepository: ExpenseRepository,
) {

    fun getExpenses(userId: UserId, vehicleId: VehicleId): List<Expense> {
        vehicleOwnerValidator.validateUserOwnVehicle(userId = userId, vehicleId = vehicleId)
        return expenseRepository.getExpenses().filter { it.vehicleId == vehicleId }
    }

    fun addExpense(newExpense: NewExpense): Expense {
        vehicleOwnerValidator.validateUserOwnVehicle(userId = newExpense.userId, vehicleId = newExpense.vehicleId)
        return expenseRepository.createExpense(newExpense)
    }
}