package pl.przemyslawpitus.brum.infrastructure

import org.springframework.stereotype.Repository
import pl.przemyslawpitus.brum.domain.entity.Expense
import pl.przemyslawpitus.brum.domain.entity.ExpenseCategory
import pl.przemyslawpitus.brum.domain.entity.UserId
import pl.przemyslawpitus.brum.domain.entity.VehicleId
import pl.przemyslawpitus.brum.domain.repository.ExpenseRepository
import java.math.BigDecimal
import java.time.Instant

@Repository
class InMemoryExpenseRepository: ExpenseRepository {
    private val expenses: MutableCollection<Expense> = mutableListOf(
        Expense(
            id = 1,
            userId = 1,
            vehicleId = 1,
            name = "Expense-1",
            description = "Some expense",
            category = ExpenseCategory.FUEL,
            amount = BigDecimal(100),
            timestamp = Instant.now(),
        )
    )

    override fun saveExpense(expense: Expense): Expense {
        expenses.add(expense)
        return expense
    }

    override fun getExpensesByUserId(userId: UserId): List<Expense> {
        return expenses.filter { it.userId == userId }
    }

    override fun getExpensesByVehicleId(vehicleId: VehicleId): List<Expense> {
        return expenses.filter { it.vehicleId == vehicleId }
    }
}