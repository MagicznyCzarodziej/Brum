package pl.przemyslawpitus.brum.domain.repository

import pl.przemyslawpitus.brum.domain.entity.Expense
import pl.przemyslawpitus.brum.domain.entity.NewExpense

interface ExpenseRepository {
    fun createExpense(newExpense: NewExpense): Expense
    fun getExpenses(): List<Expense>
}