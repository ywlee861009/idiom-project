package com.kero.idiom.domain.usecase

import com.kero.idiom.domain.model.UserStats
import com.kero.idiom.domain.repository.UserStatsRepository
import kotlinx.coroutines.flow.Flow

class GetUserStatsUseCase(
    private val repository: UserStatsRepository
) {
    operator fun invoke(): Flow<UserStats> = repository.getUserStats()
}
