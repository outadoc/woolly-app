package fr.outadoc.woolly.common.feature.preference

import com.arkivanov.decompose.ComponentContext
import fr.outadoc.woolly.common.getScope

class PreferencesComponent(
    componentContext: ComponentContext,
    private val preferenceRepository: PreferenceRepository
) : ComponentContext by componentContext {

    private val scope = getScope()

}
