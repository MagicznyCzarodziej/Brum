package pl.przemyslawpitus.brum.api

import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import pl.przemyslawpitus.brum.domain.entity.Expense
import pl.przemyslawpitus.brum.domain.entity.ExpenseCategory
import pl.przemyslawpitus.brum.domain.entity.NewExpense
import pl.przemyslawpitus.brum.domain.entity.UserDetails
import pl.przemyslawpitus.brum.domain.entity.UserId
import pl.przemyslawpitus.brum.domain.entity.VehicleId
import pl.przemyslawpitus.brum.domain.service.ExpenseService
import pl.przemyslawpitus.brum.domain.service.VehicleNotFoundException
import java.math.BigDecimal
import java.time.Instant

@RestController
class ExpenseEndpoint(
    val expenseService: ExpenseService,
) {

    @GetMapping(path = ["/vehicles/{vehicleId}/expenses"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getMyExpenses(
        @AuthenticationPrincipal userDetails: UserDetails,
        @PathVariable vehicleId: Int,
    ): ResponseEntity<*> {
        val userId = userDetails.id
        return try {
            val response = expenseService.getExpenses(
                userId = userId,
                vehicleId = vehicleId,
            ).toDto()
            ResponseEntity.ok(response)
        } catch (exception: VehicleNotFoundException) {
            ResponseEntity.notFound().build<Void>()
        }
    }

    @PostMapping(path = ["/vehicles/{vehicleId}/expenses"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun addMyExpense(
        @AuthenticationPrincipal userDetails: UserDetails,
        @RequestBody body: NewExpenseDto,
        @PathVariable vehicleId: Int,
    ): ResponseEntity<*> {
        val userId = userDetails.id

        return try {
            val response = expenseService
                .addExpense(body.toDomain(userId, vehicleId))
                .toDto()
            ResponseEntity.ok(response)
        } catch (exception: VehicleNotFoundException) {
            ResponseEntity.notFound().build<Void>()
        }
    }
}

data class NewExpenseDto(
    val name: String,
    val description: String,
    val amount: String,
    val category: String,
    val timestamp: String,
) {
    fun toDomain(userId: UserId, vehicleId: Int) = NewExpense(
        userId = userId,
        vehicleId = vehicleId,
        name = this.name,
        description = this.description,
        category = ExpenseCategory.valueOf(this.category),
        amount = BigDecimal(this.amount),
        timestamp = Instant.parse(this.timestamp),
    )
}

data class ExpenseDto(
    val vehicleId: VehicleId,
    val name: String,
    val description: String,
    val category: String,
    val amount: String,
    val timestamp: String,
)

private fun Expense.toDto() = ExpenseDto(
    vehicleId = this.vehicleId,
    name = this.name,
    description = this.description,
    category = this.category.name,
    amount = this.amount.toPlainString(),
    timestamp = this.timestamp.toString()
)

data class ExpensesDto(
    val expenses: List<ExpenseDto>,
    val totalCount: Int,
)

private fun List<Expense>.toDto() = ExpensesDto(
    expenses = this.map { it.toDto() },
    totalCount = this.size,
)