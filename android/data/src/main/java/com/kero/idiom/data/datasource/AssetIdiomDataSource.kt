package com.kero.idiom.data.datasource

import android.content.Context
import com.kero.idiom.domain.model.Idiom
import kotlinx.serialization.json.Json
import javax.inject.Inject
import dagger.hilt.android.qualifiers.ApplicationContext

class AssetIdiomDataSource @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val json = Json { ignoreUnknownKeys = true }

    fun getIdiomsFromAssets(): List<Idiom> {
        val jsonString = context.assets.open("idioms.json").bufferedReader().use { it.readText() }
        return json.decodeFromString(jsonString)
    }
}
