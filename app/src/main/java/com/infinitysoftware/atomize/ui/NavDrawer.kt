package com.infinitysoftware.atomize.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.*
import com.infinitysoftware.atomize.R
import com.infinitysoftware.atomize.model.Screens
import com.infinitysoftware.atomize.model.habit.HabitDatabase
import com.infinitysoftware.atomize.ui.about.AboutScreen
import com.infinitysoftware.atomize.ui.auth.LoginScreen
import com.infinitysoftware.atomize.ui.auth.SignupScreen
import com.infinitysoftware.atomize.ui.home.HomeScreen
import com.infinitysoftware.atomize.ui.settings.SettingsScreen
import com.infinitysoftware.atomize.ui.settings.SettingsViewModel
import com.infinitysoftware.atomize.ui.settings.SettingsViewModelFactory
import com.infinitysoftware.atomize.ui.theme.MediumGray
import com.infinitysoftware.atomize.ui.theme.PrimaryGreen
import com.infinitysoftware.atomize.ui.theme.White
import com.infinitysoftware.atomize.viewmodel.AuthViewModel
import com.infinitysoftware.atomize.viewmodel.AuthState
import com.infinitysoftware.atomize.viewmodel.AuthViewModelFactory
import com.infinitysoftware.atomize.viewmodel.HabitViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavDrawer(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val coroutineScope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val context = LocalContext.current
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val database = HabitDatabase.getDatabase(context)
    val settingsDao = database.settingsDao()
    val settingsViewModel: SettingsViewModel = viewModel(factory = SettingsViewModelFactory(settingsDao))
    val authViewModel: AuthViewModel = viewModel(factory = AuthViewModelFactory())
    val habitViewModel: HabitViewModel = viewModel()
    val authState by authViewModel.authState.observeAsState()

    val constants = Constants()
    LaunchedEffect(Unit) {
        authViewModel.onUserChanged = {
            habitViewModel.refreshDatabase()
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = true,
        drawerContent = {
            ModalDrawerSheet {
                Box(modifier = Modifier.fillMaxHeight()) {
                    Column(modifier = Modifier.align(Alignment.TopStart)) {
                        Box(
                            modifier = Modifier
                                .background(PrimaryGreen)
                                .fillMaxWidth()
                                .height(constants.navDrawerHeight)
                        ) {
                            Column(
                                modifier = Modifier.align(Alignment.BottomStart).padding(all = constants.navDrawerPadding),
                                horizontalAlignment = Alignment.Start,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(constants.navDrawerAvatarSize)
                                        .background(color = White, shape = CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = when (authState) {
                                            is AuthState.Authenticated -> authViewModel.getCurrentUserEmail()?.firstOrNull()?.uppercase() ?: "G"
                                            else -> "G"
                                        },
                                        fontSize = constants.navDrawerAvatarNameFirstCharacterSize,
                                        fontWeight = FontWeight.Bold,
                                        color = PrimaryGreen
                                    )
                                }

                                Spacer(modifier = Modifier.height(constants.navDrawerPadding))

                                Text(
                                    text = when (authState) {
                                        is AuthState.Authenticated -> authViewModel.getCurrentUserEmail() ?: "Guest Account"
                                        else -> "Guest Account"
                                    },
                                    style = MaterialTheme.typography.titleSmall,
                                    color = White,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }

                        NavigationDrawerItem(
                            label = { Text(stringResource(R.string.nav_home), fontWeight = FontWeight.Bold) },
                            selected = currentRoute == Screens.Home.screen,
                            shape = RectangleShape,
                            icon = { Icon(Icons.Default.Home, contentDescription = stringResource(R.string.nav_home)) },
                            onClick = {
                                coroutineScope.launch { drawerState.close() }
                                if (currentRoute != Screens.Home.screen) {
                                    navController.navigate(Screens.Home.screen) {
                                        popUpTo(Screens.Home.screen) { inclusive = true }
                                        launchSingleTop = true
                                    }
                                }
                            },
                            colors = NavigationDrawerItemDefaults.colors(
                                selectedContainerColor = PrimaryGreen.copy(alpha = constants.navDrawerSelectedItemAlpha),
                                selectedIconColor = PrimaryGreen,
                                selectedTextColor = PrimaryGreen,
                                unselectedIconColor = MediumGray,
                                unselectedTextColor = MediumGray
                            )
                        )

                        NavigationDrawerItem(
                            label = { Text(stringResource(R.string.nav_settings), fontWeight = FontWeight.Bold) },
                            selected = currentRoute == Screens.Settings.screen,
                            shape = RectangleShape,
                            icon = { Icon(Icons.Default.Settings, contentDescription = stringResource(R.string.nav_settings)) },
                            onClick = {
                                coroutineScope.launch { drawerState.close() }
                                if (currentRoute != Screens.Settings.screen) {
                                    navController.navigate(Screens.Settings.screen) {
                                        popUpTo(Screens.Home.screen)
                                        launchSingleTop = true
                                    }
                                }
                            },
                            colors = NavigationDrawerItemDefaults.colors(
                                selectedContainerColor = PrimaryGreen.copy(alpha = constants.navDrawerSelectedItemAlpha),
                                selectedIconColor = PrimaryGreen,
                                selectedTextColor = PrimaryGreen,
                                unselectedIconColor = MediumGray,
                                unselectedTextColor = MediumGray
                            )
                        )

                        NavigationDrawerItem(
                            label = { Text(stringResource(R.string.nav_about), fontWeight = FontWeight.Bold) },
                            selected = currentRoute == Screens.About.screen,
                            shape = RectangleShape,
                            icon = { Icon(Icons.Default.Info, contentDescription = stringResource(R.string.nav_about)) },
                            onClick = {
                                coroutineScope.launch { drawerState.close() }
                                if (currentRoute != Screens.About.screen) {
                                    navController.navigate(Screens.About.screen) {
                                        popUpTo(Screens.Home.screen)
                                        launchSingleTop = true
                                    }
                                }
                            },
                            colors = NavigationDrawerItemDefaults.colors(
                                selectedContainerColor = PrimaryGreen.copy(alpha = constants.navDrawerSelectedItemAlpha),
                                selectedIconColor = PrimaryGreen,
                                selectedTextColor = PrimaryGreen,
                                unselectedIconColor = MediumGray,
                                unselectedTextColor = MediumGray
                            )
                        )

                        when (authState) {
                            is AuthState.Authenticated -> {
                                NavigationDrawerItem(
                                    label = { Text(stringResource(R.string.nav_logout), fontWeight = FontWeight.Bold) },
                                    selected = false,
                                    shape = RectangleShape,
                                    icon = { Icon(Icons.Default.ExitToApp, contentDescription = stringResource(R.string.nav_logout)) },
                                    onClick = {
                                        coroutineScope.launch { drawerState.close() }
                                        authViewModel.signout()

                                        navController.navigate(Screens.Home.screen) {
                                            popUpTo(Screens.Home.screen) { inclusive = true }
                                            launchSingleTop = true
                                        }
                                    },
                                    colors = NavigationDrawerItemDefaults.colors(
                                        selectedContainerColor = PrimaryGreen.copy(alpha = constants.navDrawerSelectedItemAlpha),
                                        selectedIconColor = PrimaryGreen,
                                        selectedTextColor = PrimaryGreen,
                                        unselectedIconColor = MediumGray,
                                        unselectedTextColor = MediumGray
                                    )
                                )
                            }

                            else -> {
                                NavigationDrawerItem(
                                    label = { Text(stringResource(R.string.nav_login), fontWeight = FontWeight.Bold) },
                                    selected = currentRoute == Screens.Login.screen,
                                    shape = RectangleShape,
                                    icon = { Icon(Icons.Default.Login, contentDescription = stringResource(R.string.nav_login)) },
                                    onClick = {
                                        coroutineScope.launch { drawerState.close() }
                                        if (currentRoute != Screens.Login.screen) {
                                            navController.navigate(Screens.Login.screen) {
                                                popUpTo(Screens.Home.screen)
                                                launchSingleTop = true
                                            }
                                        }
                                    },
                                    colors = NavigationDrawerItemDefaults.colors(
                                        selectedContainerColor = PrimaryGreen.copy(alpha = constants.navDrawerSelectedItemAlpha),
                                        selectedIconColor = PrimaryGreen,
                                        selectedTextColor = PrimaryGreen,
                                        unselectedIconColor = MediumGray,
                                        unselectedTextColor = MediumGray
                                    )
                                )
                            }
                        }
                    }

                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = constants.applicationVersionPadding)
                    ) {
                        Text(
                            text = stringResource(
                                R.string.nav_version,
                                com.infinitysoftware.atomize.BuildConfig.VERSION_NAME
                            ),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MediumGray
                        )
                    }
                }
            }
        }
    ) {
        NavHost(
            navController = navController,
            startDestination = Screens.Home.screen,
            modifier = modifier
        ) {
            composable(route = Screens.Home.screen) {
                HomeScreen(
                    navController = navController,
                    settingsViewModel = settingsViewModel,
                    authViewModel = authViewModel,
                    onMenuClick = {
                        coroutineScope.launch {
                            if (drawerState.isClosed) drawerState.open() else drawerState.close()
                        }
                    }
                )
            }
            composable(route = Screens.Settings.screen) { SettingsScreen(settingsViewModel) }
            composable(route = Screens.About.screen) { AboutScreen() }
            composable(route = Screens.Login.screen) { LoginScreen(navController, authViewModel) }
            composable(route = Screens.Signup.screen) { SignupScreen(navController, authViewModel) }
        }
    }
}