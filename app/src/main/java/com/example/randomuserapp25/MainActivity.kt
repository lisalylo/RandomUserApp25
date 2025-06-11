package com.example.randomuserapp25

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.randomuserapp25.viewUI.AddUserScreen
import com.example.randomuserapp25.viewUI.DetailScreen
import com.example.randomuserapp25.viewUI.EditUserScreen
import com.example.randomuserapp25.viewUI.OverviewScreen
import dagger.hilt.android.AndroidEntryPoint

/*@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppNavigation()
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "overview"
    ) {
        composable("overview") {
            OverviewScreen(navController)
        }
        composable("detail/{userId}") { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: return@composable
            DetailScreen(userId = userId, navController = navController)
        }
        composable("add") { AddUserScreen(navController = navController) }
    }
}*/
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            NavHost(navController, startDestination = "overview") {
                composable("overview") {
                    OverviewScreen(navController)
                }
                composable(
                    "detail/{userId}",
                    arguments = listOf(navArgument("userId") { type = NavType.StringType })
                ) { back ->
                    DetailScreen(
                        userId = back.arguments!!.getString("userId")!!,
                        navController = navController
                    )
                }
                composable(
                    "edit/{userId}",
                    arguments = listOf(navArgument("userId") { type = NavType.StringType })
                ) { back ->
                    EditUserScreen(
                        navController = navController,
                        userId = back.arguments!!.getString("userId")!!
                    )
                }
            }
        }
    }
}
