package pl.przemyslawpitus.brum.domain.entity

import java.math.BigDecimal
import java.time.Instant

typealias ExpenseId = Int

data class Expense (
    val id: ExpenseId,
    val userId: UserId,
    val vehicleId: VehicleId,
    val name: String,
    val description: String,
    val category: ExpenseCategory,
    val amount: BigDecimal,
    val timestamp: Instant,
)

enum class ExpenseCategory {
    FUEL, MAINTENANCE, SERVICE, PARTS, OTHER,
}

data class NewExpense (
    val userId: UserId,
    val vehicleId: VehicleId,
    val name: String,
    val description: String,
    val category: ExpenseCategory,
    val amount: BigDecimal,
    val timestamp: Instant,
)
