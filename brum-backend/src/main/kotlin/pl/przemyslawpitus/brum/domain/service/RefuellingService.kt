package pl.przemyslawpitus.brum.domain.service

import org.springframework.stereotype.Service
import pl.przemyslawpitus.brum.domain.entity.ExpenseCategory
import pl.przemyslawpitus.brum.domain.entity.NewExpense
import pl.przemyslawpitus.brum.domain.entity.NewMileageRecord
import pl.przemyslawpitus.brum.domain.entity.NewRefuelling
import pl.przemyslawpitus.brum.domain.entity.Refuelling
import pl.przemyslawpitus.brum.domain.entity.UserId
import pl.przemyslawpitus.brum.domain.entity.VehicleId
import pl.przemyslawpitus.brum.domain.repository.RefuellingRepository

@Service
class RefuellingService(
    private val vehicleOwnerValidator: VehicleOwnerValidator,
    private val refuellingRepository: RefuellingRepository,
    private val mileageRecordService: MileageRecordService,
    private val expenseService: ExpenseService,
) {

    fun addRefuelling(newRefuelling: NewRefuelling): Refuelling {
        vehicleOwnerValidator.validateUserOwnVehicle(userId = newRefuelling.userId, vehicleId = newRefuelling.vehicleId)

        return refuellingRepository.createRefuelling(newRefuelling).also {
            if (newRefuelling.currentMileage != null) {
                createMileageRecordForRefuelling(newRefuelling)
            }
            if (newRefuelling.createExpense) {
                createExpenseForRefuelling(newRefuelling)
            }
        }
    }

    fun getRefuellings(userId: UserId, vehicleId: VehicleId): List<Refuelling> {
        vehicleOwnerValidator.validateUserOwnVehicle(userId = userId, vehicleId = vehicleId)
        return refuellingRepository.getRefuellings().filter { it.vehicleId == vehicleId }
    }

    private fun createExpenseForRefuelling(newRefuelling: NewRefuelling) {
        if (newRefuelling.expense == null) throw MissingRefuellingExpense()
        val expense = NewExpense(
            userId = newRefuelling.userId,
            vehicleId = newRefuelling.vehicleId,
            timestamp = newRefuelling.timestamp,
            name = "Refuelling",
            description = "",
            amount = newRefuelling.expense.amount,
            category = ExpenseCategory.FUEL,
        )
        expenseService.addExpense(expense)
    }

    private fun createMileageRecordForRefuelling(newRefuelling: NewRefuelling) {
        mileageRecordService.createMileageRecord(
            NewMileageRecord(
                userId = newRefuelling.userId,
                vehicleId = newRefuelling.vehicleId,
                timestamp = newRefuelling.timestamp,
                mileage = newRefuelling.currentMileage!!,
            )
        )
    }
}

class MissingRefuellingExpense() : RuntimeException("Missing expense field in new refuelling")