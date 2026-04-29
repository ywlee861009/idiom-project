package com.kero.idiom.feature.quiz.viewmodel

import com.kero.idiom.domain.model.Idiom
import com.kero.idiom.domain.model.Quiz
import com.kero.idiom.domain.model.QuizType
import com.kero.idiom.domain.model.UserStats
import com.kero.idiom.domain.repository.DailyRecordRepository
import com.kero.idiom.domain.repository.IdiomRepository
import com.kero.idiom.domain.repository.UserStatsRepository
import com.kero.idiom.domain.model.DailyRecord
import com.kero.idiom.domain.usecase.GetRandomQuizUseCase
import com.kero.idiom.domain.usecase.GetUserStatsUseCase
import com.kero.idiom.domain.usecase.RecordCorrectAnswerUseCase
import com.kero.idiom.domain.usecase.UpdateUserStatsUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeUserStatsRepository : UserStatsRepository {
    val stats = MutableStateFlow(UserStats())
    override fun getUserStats(): Flow<UserStats> = stats
    override suspend fun updateStats(correctCount: Int, solvedCount: Int, xpGained: Int, comboCount: Int) {}
    override suspend fun updateNotificationEnabled(enabled: Boolean) {}
}

class FakeIdiomRepository : IdiomRepository {
    val idioms = mutableListOf<Idiom>()
    val correctAnswerWords = mutableListOf<String>()
    override suspend fun syncIfNeeded(onStatusChanged: ((String) -> Unit)?) {}
    override suspend fun getRandomIdioms(limit: Int) = idioms.shuffled().take(limit)
    override suspend fun recordExposure(word: String) {}
    override suspend fun recordCorrectAnswer(word: String) { correctAnswerWords.add(word) }
    override suspend fun getAllIdioms() = idioms
    override suspend fun getAcquiredIdioms() = emptyList<Idiom>()
}

class FakeDailyRecordRepository : DailyRecordRepository {
    override suspend fun recordToday(solvedCount: Int, correctCount: Int, earnedXp: Int) {}
    override suspend fun getWeeklyRecords() = emptyList<DailyRecord>()
    override suspend fun getMonthlyRecords(yearMonth: String) = emptyList<DailyRecord>()
}

val sampleIdioms = listOf(
    Idiom("일석이조", "一石二鳥", "하나의 돌로 두 마리 새를 잡음", 1),
    Idiom("사면초가", "四面楚歌", "사방이 모두 적에게 둘러싸임", 2),
    Idiom("오매불망", "寤寐不忘", "자나 깨나 잊지 못함", 1),
    Idiom("사필귀정", "事必歸正", "모든 일은 반드시 바른 길로 돌아옴", 2),
    Idiom("고진감래", "苦盡甘來", "고생 끝에 즐거움이 옴", 1),
    Idiom("삼고초려", "三顧草廬", "인재를 얻기 위해 참을성 있게 노력함", 2),
    Idiom("유비무환", "有備無患", "준비가 있으면 걱정이 없음", 1),
    Idiom("자업자득", "自業自得", "자기가 저지른 일의 결과를 자기가 받음", 1),
    Idiom("동고동락", "同苦同樂", "괴로움과 즐거움을 함께 함", 1),
    Idiom("온고지신", "溫故知新", "옛것을 익혀 새것을 앎", 2),
    Idiom("풍전등화", "風前燈火", "매우 위태로운 처지", 1),
    Idiom("이심전심", "以心傳心", "마음에서 마음으로 전함", 1),
    Idiom("격물치지", "格物致知", "사물의 이치를 연구하여 지식을 넓힘", 3),
    Idiom("대기만성", "大器晩成", "큰 인물은 늦게 이루어짐", 2),
    Idiom("마이동풍", "馬耳東風", "남의 말을 귀담아듣지 않음", 1),
    Idiom("역지사지", "易地思之", "상대방의 처지에서 생각함", 1),
    Idiom("청출어람", "靑出於藍", "제자가 스승보다 나음", 2),
    Idiom("결초보은", "結草報恩", "죽어서도 은혜를 갚음", 2),
    Idiom("조삼모사", "朝三暮四", "간사한 꾀로 남을 속임", 1),
    Idiom("백문불여일견", "百聞不如一見", "백 번 듣는 것이 한 번 보는 것만 못함", 2),
)

fun createTestDependencies(): TestDependencies {
    val fakeIdiomRepo = FakeIdiomRepository().apply { idioms.addAll(sampleIdioms) }
    val fakeUserStatsRepo = FakeUserStatsRepository()
    val fakeDailyRecordRepo = FakeDailyRecordRepository()
    val fakeAdController = FakeAdController()

    return TestDependencies(
        getRandomQuizUseCase = GetRandomQuizUseCase(fakeIdiomRepo),
        recordCorrectAnswerUseCase = RecordCorrectAnswerUseCase(fakeIdiomRepo),
        updateUserStatsUseCase = UpdateUserStatsUseCase(fakeUserStatsRepo, fakeDailyRecordRepo),
        getUserStatsUseCase = GetUserStatsUseCase(fakeUserStatsRepo),
        adController = fakeAdController,
        idiomRepo = fakeIdiomRepo,
        userStatsRepo = fakeUserStatsRepo,
    )
}

data class TestDependencies(
    val getRandomQuizUseCase: GetRandomQuizUseCase,
    val recordCorrectAnswerUseCase: RecordCorrectAnswerUseCase,
    val updateUserStatsUseCase: UpdateUserStatsUseCase,
    val getUserStatsUseCase: GetUserStatsUseCase,
    val adController: FakeAdController,
    val idiomRepo: FakeIdiomRepository,
    val userStatsRepo: FakeUserStatsRepository,
)
