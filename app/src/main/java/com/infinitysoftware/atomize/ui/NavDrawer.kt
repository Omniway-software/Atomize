package com.infinitysoftware.atomize.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.filled.AppRegistration
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
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
    val database = remember { HabitDatabase.getDatabase(context) }
    val settingsDao = remember { database.settingsDao() }
    val settingsViewModel: SettingsViewModel = viewModel(
        factory = SettingsViewModelFactory(settingsDao)
    )

    val constants = Constants()

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
                                .height(constants.navDrawerHeight),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = stringResource(id = R.string.app_name),
                                style = MaterialTheme.typography.titleLarge,
                                color = White
                            )
                        }
                        NavigationDrawerItem(
                            label = { Text(stringResource(R.string.nav_home), fontWeight = FontWeight.Bold) },
                            selected = currentRoute == Screens.Home.screen,
                            shape = RectangleShape,
                            icon = { Icon(Icons.Default.Home, contentDescription = stringResource(R.string.cd_home)) },
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
                            icon = { Icon(Icons.Default.Settings, contentDescription = stringResource(R.string.cd_settings)) },
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
                            icon = { Icon(Icons.Default.Info, contentDescription = stringResource(R.string.cd_about)) },
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
                        NavigationDrawerItem(
                            label = { Text(stringResource(R.string.nav_login), fontWeight = FontWeight.Bold) },
                            selected = currentRoute == Screens.Login.screen,
                            shape = RectangleShape,
                            icon = { Icon(Icons.Default.Login, contentDescription = stringResource(R.string.cd_login)) },
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
                        NavigationDrawerItem(
                            label = { Text(stringResource(R.string.nav_signup), fontWeight = FontWeight.Bold) },
                            selected = currentRoute == Screens.Signup.screen,
                            shape = RectangleShape,
                            icon = { Icon(Icons.Default.PersonAdd, contentDescription = stringResource(R.string.cd_signup)) },
                            onClick = {
                                coroutineScope.launch { drawerState.close() }
                                if (currentRoute != Screens.Signup.screen) {
                                    navController.navigate(Screens.Signup.screen) {
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
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = constants.applicationVersionPadding)
                    ) {
                        Text(
                            text = stringResource(R.string.nav_version, com.infinitysoftware.atomize.BuildConfig.VERSION_NAME),
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
                    onMenuClick = {
                        coroutineScope.launch {
                            if (drawerState.isClosed) {
                                drawerState.open()
                            } else {
                                drawerState.close()
                            }
                        }
                    }
                )
            }
            composable(route = Screens.Settings.screen) {
                SettingsScreen(viewModel = settingsViewModel)
            }
            composable(route = Screens.About.screen) {
                AboutScreen()
            }
            composable(route = Screens.Login.screen) {
                LoginScreen(navController)
            }
            composable(route = Screens.Signup.screen) {
                SignupScreen(navController)
            }
        }
    }
}