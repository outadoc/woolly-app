package fr.outadoc.woolly.ui.feature.search

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import fr.outadoc.woolly.common.feature.mainrouter.AppScreen
import fr.outadoc.woolly.common.feature.search.SearchSubScreen
import fr.outadoc.woolly.common.feature.search.component.SearchComponent
import fr.outadoc.woolly.ui.MR
import fr.outadoc.woolly.ui.mainrouter.TopAppBarWithMenu
import fr.outadoc.woolly.ui.screen.getIcon
import fr.outadoc.woolly.ui.screen.getTitle
import fr.outadoc.woolly.ui.strings.stringResource
import kotlinx.coroutines.launch

@Composable
fun SearchTopAppBar(
    modifier: Modifier = Modifier,
    component: SearchComponent,
    drawerState: DrawerState?,
    contentPadding: PaddingValues = PaddingValues()
) {
    val state by component.state.collectAsState()

    val textStyle = LocalTextStyle.current
    val scope = rememberCoroutineScope()

    Surface(
        color = MaterialTheme.colors.primarySurface,
        elevation = AppBarDefaults.TopAppBarElevation
    ) {
        Column {
            TopAppBarWithMenu(
                modifier = modifier,
                contentPadding = contentPadding,
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
                    onCurrentSubScreenChanged = { subScreen ->
                        scope.launch {
                            component.onSubScreenSelected(subScreen)
                        }
                    }
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
                Text(text = screen.getTitle())
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
            Icon(
                AppScreen.Search.getIcon(),
                contentDescription = AppScreen.Search.getTitle()
            )
        },
        placeholder = { Text(stringResource(MR.strings.search_input_placeholder)) },
        trailingIcon = {
            if (searchTerm.isNotEmpty()) {
                IconButton(onClick = { onSearchTermChanged("") }) {
                    Icon(
                        Icons.Default.Clear,
                        contentDescription = stringResource(MR.strings.search_input_clear_cd)
                    )
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
