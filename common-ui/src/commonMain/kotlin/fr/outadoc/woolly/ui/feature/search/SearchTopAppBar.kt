package fr.outadoc.woolly.ui.feature.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import fr.outadoc.woolly.common.feature.search.SearchScreenResources
import fr.outadoc.woolly.common.feature.search.SearchSubScreen
import fr.outadoc.woolly.common.feature.search.component.SearchComponent
import fr.outadoc.woolly.ui.navigation.TopAppBarWithMenu
import org.kodein.di.compose.instance

@Composable
fun SearchTopAppBar(
    component: SearchComponent,
    drawerState: DrawerState?
) {
    val state by component.state.collectAsState()

    val textStyle = LocalTextStyle.current

    Surface(
        color = MaterialTheme.colors.primarySurface,
        elevation = AppBarDefaults.TopAppBarElevation
    ) {
        Column {
            TopAppBarWithMenu(
                backgroundColor = MaterialTheme.colors.primarySurface,
                title = {
                    ProvideTextStyle(value = textStyle) {
                        SearchTextField(
                            searchTerm = state.query,
                            onSearchTermChanged = {
                                component.onSearchTermChanged(it)
                            }
                        )
                    }
                },
                drawerState = drawerState,
                elevation = 0.dp
            )

            if (state.query.isNotEmpty()) {
                SearchTabRow(
                    currentSubScreen = state.subScreen,
                    onCurrentSubScreenChanged = component::onSubScreenSelected
                )
            }
        }
    }
}

@Composable
fun SearchTabRow(
    currentSubScreen: SearchSubScreen,
    onCurrentSubScreenChanged: (SearchSubScreen) -> Unit,
) {
    val searchRes by instance<SearchScreenResources>()

    val tabs = listOf(
        SearchSubScreen.Statuses,
        SearchSubScreen.Accounts,
        SearchSubScreen.Hashtags
    )

    TabRow(selectedTabIndex = tabs.indexOf(currentSubScreen)) {
        tabs.forEach { screen ->
            Tab(
                modifier = Modifier.height(48.dp),
                selected = currentSubScreen == screen,
                onClick = {
                    onCurrentSubScreenChanged(screen)
                }
            ) {
                Text(text = searchRes.getScreenTitle(screen))
            }
        }
    }
}

@Composable
fun SearchTextField(
    searchTerm: String,
    onSearchTermChanged: (String) -> Unit
) {
    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        value = searchTerm,
        onValueChange = { term -> onSearchTermChanged(term) },
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 4.dp),
        singleLine = true,
        leadingIcon = {
            Icon(Icons.Default.Search, "Search")
        },
        placeholder = { Text("Search for something…") },
        trailingIcon = {
            if (searchTerm.isNotEmpty()) {
                IconButton(onClick = { onSearchTermChanged("") }) {
                    Icon(Icons.Default.Clear, "Clear search")
                }
            }
        },
        keyboardActions = KeyboardActions {
            focusManager.clearFocus()
        },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search
        ),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            cursorColor = LocalContentColor.current,
            leadingIconColor = LocalContentColor.current,
            placeholderColor = LocalContentColor.current,
            trailingIconColor = LocalContentColor.current,
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent
        )
    )
}
