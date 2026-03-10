package com.kero.idiom.data.datasource

import com.kero.idiom.data.resources.Res
import com.kero.idiom.domain.model.Idiom
import kotlinx.serialization.json.Json
import org.jetbrains.compose.resources.ExperimentalResourceApi

class AssetIdiomDataSource {
    private val json = Json { ignoreUnknownKeys = true }

    @OptIn(ExperimentalResourceApi::class)
    suspend fun getIdiomsFromAssets(): List<Idiom> {
        val bytes = Res.readBytes("files/idioms.json")
        return json.decodeFromString(bytes.decodeToString())
    }
}
