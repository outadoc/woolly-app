package fr.outadoc.woolly.common.feature.preference

import net.harawata.appdirs.AppDirsFactory
import java.io.File

actual object PreferenceFileProvider {

    private val configPath = AppDirsFactory.getInstance()
        .getUserConfigDir(
            "Woolly",
            null,
            "outadoc",
            true
        )

    private val directory = File(configPath).apply { mkdirs() }

    actual val preferenceFile: File? = File(directory, "main.preferences_pb")
}
