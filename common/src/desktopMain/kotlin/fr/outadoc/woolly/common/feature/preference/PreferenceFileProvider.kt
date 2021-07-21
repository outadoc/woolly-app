package fr.outadoc.woolly.common.feature.preference

import java.io.File

actual object PreferenceFileProvider {

    actual val preferenceFile: File?
        get() {
            val directory = File(
                System.getProperty(
                    "java.util.prefs.userRoot",
                    System.getProperty("user.home")
                ),
                ".config/fr.outadoc.woolly"
            ).apply { mkdirs() }

            return File(directory, "main.preferences_pb")
        }
}
