package fr.outadoc.woolly.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.lifecycle.lifecycleScope
import fr.outadoc.woolly.common.App
import fr.outadoc.woolly.common.feature.preference.AndroidPreferenceRepositoryImpl
import fr.outadoc.woolly.common.feature.preference.PreferenceRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.serialization.json.Json
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.compose.withDI
import org.kodein.di.instance

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { AndroidApp() }
    }

    @Composable
    fun AndroidApp() = withDI(di) {
        App()
    }

    private val di = DI {

        bindSingleton { Json {} }
        bindSingleton<CoroutineScope> { lifecycleScope }

        bindSingleton<PreferenceRepository> {
            AndroidPreferenceRepositoryImpl(
                context = this@MainActivity,
                json = instance()
            )
        }
    }
}
