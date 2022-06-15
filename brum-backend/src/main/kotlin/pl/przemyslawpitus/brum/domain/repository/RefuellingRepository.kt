package pl.przemyslawpitus.brum.domain.repository

import pl.przemyslawpitus.brum.domain.entity.NewRefuelling
import pl.przemyslawpitus.brum.domain.entity.Refuelling

interface RefuellingRepository {
    fun createRefuelling(newRefuelling: NewRefuelling): Refuelling
    fun getRefuellings(): List<Refuelling>
}