package com.infinitysoftware.atomize.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.infinitysoftware.atomize.R
import com.infinitysoftware.atomize.model.Screens
import com.infinitysoftware.atomize.ui.about.AboutFragment
import com.infinitysoftware.atomize.ui.home.HomeFragment
import com.infinitysoftware.atomize.ui.settings.SettingsFragment
import com.infinitysoftware.atomize.ui.theme.PrimaryGreen
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
                                .height(64.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = stringResource(id = R.string.app_name),
                                style = MaterialTheme.typography.titleLarge,
                                color = Color.White
                            )
                        }
                        HorizontalDivider()
                        NavigationDrawerItem(
                            label = { Text("Home") },
                            selected = currentRoute == Screens.Home.screen,
                            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
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
                                selectedContainerColor = PrimaryGreen.copy(alpha = 0.1f),
                                selectedIconColor = PrimaryGreen,
                                selectedTextColor = PrimaryGreen,
                                unselectedIconColor = Color.Gray,
                                unselectedTextColor = Color.Gray
                            )
                        )
                        NavigationDrawerItem(
                            label = { Text("Settings") },
                            selected = currentRoute == Screens.Settings.screen,
                            icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
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
                                selectedContainerColor = PrimaryGreen.copy(alpha = 0.1f),
                                selectedIconColor = PrimaryGreen,
                                selectedTextColor = PrimaryGreen,
                                unselectedIconColor = Color.Gray,
                                unselectedTextColor = Color.Gray
                            )
                        )
                        NavigationDrawerItem(
                            label = { Text("About") },
                            selected = currentRoute == Screens.About.screen,
                            icon = { Icon(Icons.Default.Info, contentDescription = "About") },
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
                                selectedContainerColor = PrimaryGreen.copy(alpha = 0.1f),
                                selectedIconColor = PrimaryGreen,
                                selectedTextColor = PrimaryGreen,
                                unselectedIconColor = Color.Gray,
                                unselectedTextColor = Color.Gray
                            )
                        )
                        NavigationDrawerItem(
                            label = { Text("Exit") },
                            selected = false,
                            icon = { Icon(Icons.Default.ExitToApp, contentDescription = "Exit") },
                            onClick = {
                                coroutineScope.launch { drawerState.close() }
                                Toast.makeText(context, "Exit", Toast.LENGTH_SHORT).show()
                            },
                            colors = NavigationDrawerItemDefaults.colors(
                                unselectedIconColor = Color.Red,
                                unselectedTextColor = Color.Red
                            )
                        )
                    }
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 16.dp)
                    ) {
                        Text(
                            text = "Version: ${com.infinitysoftware.atomize.BuildConfig.VERSION_NAME}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
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
                HomeFragment(
                    navController = navController,
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
                SettingsFragment()
            }
            composable(route = Screens.About.screen) {
                AboutFragment()
            }
        }
    }
}
