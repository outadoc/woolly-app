package fr.outadoc.woolly.common.feature.preference

import java.io.File

expect object PreferenceFileProvider {
    val preferenceFile: File?
}
