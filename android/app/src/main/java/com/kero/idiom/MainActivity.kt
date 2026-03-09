package com.kero.idiom

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.kero.idiom.core.components.CultureLoadingScreen
import com.kero.idiom.core.theme.IdiomQuizTheme
import com.kero.idiom.domain.repository.IdiomRepository
import com.kero.idiom.ui.navigation.IdiomNavGraph
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var repository: IdiomRepository

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            IdiomQuizTheme {
                var isSyncing by remember { mutableStateOf(true) }

                LaunchedEffect(Unit) {
                    repository.syncIfNeeded()
                    delay(2000)
                    isSyncing = false
                }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (isSyncing) {
                        CultureLoadingScreen()
                    } else {
                        IdiomNavGraph()
                    }
                }
            }
        }
    }
}
