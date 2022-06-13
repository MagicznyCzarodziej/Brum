package pl.przemyslawpitus.brum.domain.repository

import pl.przemyslawpitus.brum.domain.entity.Expense
import pl.przemyslawpitus.brum.domain.entity.UserId
import pl.przemyslawpitus.brum.domain.entity.VehicleId

interface ExpenseRepository {
    fun saveExpense(expense: Expense): Expense
    fun getExpensesByUserId(userId: UserId): List<Expense>
    fun getExpensesByVehicleId(vehicleId: VehicleId): List<Expense>
}