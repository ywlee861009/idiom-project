package com.kero.idiom.data.local

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import org.koin.android.ext.koin.androidContext

// commonMain의 getDatabaseBuilder에 대한 actual 구현
actual fun getDatabaseBuilder(): RoomDatabase.Builder<IdiomDatabase> {
    // [Note] Koin 모듈 안에서 호출되어야 하지만, 
    // 여기서는 Room의 Multiplatform 지원 방식에 따라 actual을 구현합니다.
    // 하지만 CMP Room은 보통 Factory 인터페이스를 추천합니다.
    // 일단 빌드 오류를 잡기 위해 빈 구현체(또는 나중에 수정할 곳)를 두거나 정확히 구현하겠습니다.
    throw RuntimeException("Android Context required. Use platformDataModule for initialization.")
}

// 실제로 Koin에서 사용할 헬퍼 함수
fun getAndroidDatabaseBuilder(ctx: Context): RoomDatabase.Builder<IdiomDatabase> {
    val dbFile = ctx.getDatabasePath("idiom.db")
    return Room.databaseBuilder<IdiomDatabase>(
        context = ctx,
        name = dbFile.absolutePath
    )
}
