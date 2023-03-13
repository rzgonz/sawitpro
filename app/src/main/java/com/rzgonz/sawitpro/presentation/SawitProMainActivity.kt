package com.rzgonz.sawitpro.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rzgonz.sawitpro.presentation.main.MainScreen
import com.rzgonz.sawitpro.presentation.ocr.OcrScreen
import com.rzgonz.sawitpro.ui.theme.SawitProTheme


class SawitProMainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SawitProTheme {
                // A surface container using the 'background' color from the theme
                SawitProNavigation(navController = rememberNavController())
            }
        }
    }
}


@Composable
fun SawitProNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        route = "sawitpro_graph",
        startDestination = SawitProScreenNav.Home.route
    ) {
        composable(route = SawitProScreenNav.Home.route) {
            MainScreen(navController)
        }
        composable(route = SawitProScreenNav.Ocr.route) {
            OcrScreen(navController)
        }
    }
}

