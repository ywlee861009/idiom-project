package com.kero.idiom.data.datasource

import com.kero.idiom.domain.model.Idiom
import kotlinx.serialization.json.Json
// import org.jetbrains.compose.resources.ExperimentalResourceApi (needed for some versions)
// import idiom_cmp.composeapp.generated.resources.Res (This will be generated)

class AssetIdiomDataSource {
    private val json = Json { ignoreUnknownKeys = true }

    // suspend로 변경하여 CMP 리소스 로딩에 최적화
    suspend fun getIdiomsFromAssets(): List<Idiom> {
        // [Note] Res.readBytes("files/idioms.json")를 사용하도록 나중에 연결할 예정입니다.
        // 일단은 인터페이스만 맞춰놓겠습니다. (Context 제거가 핵심!)
        return emptyList() 
    }
}
