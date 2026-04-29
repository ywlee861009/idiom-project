package com.kero.idiom.domain.usecase

import com.kero.idiom.domain.model.Idiom
import com.kero.idiom.domain.model.QuizType
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/** 퀴즈 생성 UseCase가 5가지 유형의 퀴즈를 올바른 구조로 생성하는지 검증 */
class GetRandomQuizUseCaseTest {

    private val fakeRepo = FakeIdiomRepository()
    private val useCase = GetRandomQuizUseCase(fakeRepo)

    private val sampleIdioms = listOf(
        Idiom("일석이조", "一石二鳥", "하나의 돌로 두 마리 새를 잡음", 1),
        Idiom("사면초가", "四面楚歌", "사방이 모두 적에게 둘러싸임", 2),
        Idiom("오매불망", "寤寐不忘", "자나 깨나 잊지 못함", 1),
        Idiom("사필귀정", "事必歸正", "모든 일은 반드시 바른 길로 돌아옴", 2),
        Idiom("고진감래", "苦盡甘來", "고생 끝에 즐거움이 옴", 1),
        Idiom("삼고초려", "三顧草廬", "인재를 얻기 위해 참을성 있게 노력함", 2),
        Idiom("유비무환", "有備無患", "준비가 있으면 걱정이 없음", 1),
        Idiom("백문불여일견", "百聞不如一見", "백 번 듣는 것이 한 번 보는 것만 못함", 2),
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
    )

    /** 퀴즈 생성 시 유효한 Quiz 객체가 반환되고, 퀴즈 유형이 5가지 중 하나인지 검증 */
    @Test
    fun invoke_returnsQuiz() = runTest {
        fakeRepo.idioms.addAll(sampleIdioms)

        val quiz = useCase()

        assertNotNull(quiz)
        assertTrue(QuizType.entries.contains(quiz.type))
    }

    /** 퀴즈 생성 시 출제된 사자성어의 노출 기록(exposure)이 저장되는지 검증 */
    @Test
    fun invoke_recordsExposure() = runTest {
        fakeRepo.idioms.addAll(sampleIdioms)

        useCase()

        assertEquals(1, fakeRepo.exposedWords.size)
    }

    /** DB가 비어있을 때 IllegalStateException이 발생하는지 검증 (데이터 없는 상태 방어) */
    @Test
    fun invoke_emptyDB_throwsException() = runTest {
        assertFailsWith<IllegalStateException> {
            useCase()
        }
    }

    /** FILL_BLANK(한 글자 빈칸 객관식): 보기 4개, 빈칸 1개, 정답이 보기에 포함되는지 검증 */
    @Test
    fun invoke_fillBlank_hasCorrectStructure() = runTest {
        fakeRepo.idioms.addAll(sampleIdioms)

        repeat(50) {
            val quiz = useCase()
            if (quiz.type == QuizType.FILL_BLANK) {
                assertEquals(4, quiz.options.size)
                assertEquals(1, quiz.blankIndices.size)
                assertTrue(quiz.questionText.contains('_'))
                assertTrue(quiz.options.contains(quiz.answer))
                return@runTest
            }
        }
    }

    /** MEANING_TO_WORD(뜻→단어 객관식): 뜻이 문제로, 보기 4개에 정답 포함 검증 */
    @Test
    fun invoke_meaningToWord_hasCorrectStructure() = runTest {
        fakeRepo.idioms.addAll(sampleIdioms)

        repeat(50) {
            val quiz = useCase()
            if (quiz.type == QuizType.MEANING_TO_WORD) {
                assertEquals(4, quiz.options.size)
                assertEquals(quiz.originalIdiom.meaning, quiz.questionText)
                assertTrue(quiz.options.contains(quiz.answer))
                return@runTest
            }
        }
    }

    /** HANJA_TO_HANGUL(한자→한글 객관식): 한자가 문제로, 보기 4개에 정답 포함 검증 */
    @Test
    fun invoke_hanjaToHangul_hasCorrectStructure() = runTest {
        fakeRepo.idioms.addAll(sampleIdioms)

        repeat(50) {
            val quiz = useCase()
            if (quiz.type == QuizType.HANJA_TO_HANGUL) {
                assertEquals(4, quiz.options.size)
                assertEquals(quiz.originalIdiom.hanja, quiz.questionText)
                assertTrue(quiz.options.contains(quiz.answer))
                return@runTest
            }
        }
    }

    /** FILL_BLANKS_2(주관식 2칸): 빈칸 2개, 보기 없음, 정답이 원본 단어와 일치하는지 검증 */
    @Test
    fun invoke_fillBlanks2_hasCorrectStructure() = runTest {
        fakeRepo.idioms.addAll(sampleIdioms)

        repeat(50) {
            val quiz = useCase()
            if (quiz.type == QuizType.FILL_BLANKS_2) {
                assertEquals(2, quiz.blankIndices.size)
                assertTrue(quiz.options.isEmpty())
                assertEquals(quiz.originalIdiom.word, quiz.answer)
                return@runTest
            }
        }
    }

    /** FILL_BLANKS_4(주관식 4칸): 빈칸 4개, 보기 없음, 정답이 원본 단어와 일치하는지 검증 */
    @Test
    fun invoke_fillBlanks4_hasCorrectStructure() = runTest {
        fakeRepo.idioms.addAll(sampleIdioms)

        repeat(50) {
            val quiz = useCase()
            if (quiz.type == QuizType.FILL_BLANKS_4) {
                assertEquals(4, quiz.blankIndices.size)
                assertTrue(quiz.options.isEmpty())
                assertEquals(quiz.originalIdiom.word, quiz.answer)
                return@runTest
            }
        }
    }
}
