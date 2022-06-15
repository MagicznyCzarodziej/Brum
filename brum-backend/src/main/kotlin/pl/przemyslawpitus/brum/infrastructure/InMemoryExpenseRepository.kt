package pl.przemyslawpitus.brum.infrastructure

import org.springframework.stereotype.Repository
import pl.przemyslawpitus.brum.domain.entity.Expense
import pl.przemyslawpitus.brum.domain.entity.ExpenseCategory
import pl.przemyslawpitus.brum.domain.entity.ExpenseId
import pl.przemyslawpitus.brum.domain.entity.NewExpense
import pl.przemyslawpitus.brum.domain.repository.ExpenseRepository
import java.math.BigDecimal
import java.time.Instant

@Repository
class InMemoryExpenseRepository : ExpenseRepository {
    private val expenses: MutableList<Expense> = mutableListOf(
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

    override fun createExpense(newExpense: NewExpense): Expense {
        val expense = Expense(
            id = getNextId(),
            userId = newExpense.userId,
            vehicleId = newExpense.vehicleId,
            name = newExpense.name,
            description = newExpense.description,
            category = newExpense.category,
            amount = newExpense.amount,
            timestamp = newExpense.timestamp,
        )
        expenses.add(expense)
        return expense
    }

    override fun getExpenses(): List<Expense> {
        return expenses
    }

    private fun getNextId(): ExpenseId {
        return expenses.size+1
    }
}