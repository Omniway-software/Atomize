package com.infinitysoftware.atomize.model

sealed class Screens(val screen: String) {
    data object Home: Screens(screen = "home")
    data object Settings: Screens(screen = "settings")
    data object About: Screens(screen = "about")
    data object Login: Screens(screen = "login")
    data object Signup: Screens(screen = "signup")
    data object Paywall: Screens(screen = "paywall")
}