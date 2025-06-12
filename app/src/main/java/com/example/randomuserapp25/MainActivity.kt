package com.example.randomuserapp25

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.randomuserapp25.arUI.ArScreen
import com.example.randomuserapp25.viewUI.DetailScreen
import com.example.randomuserapp25.viewUI.EditUserScreen
import com.example.randomuserapp25.viewUI.OverviewScreen
import dagger.hilt.android.AndroidEntryPoint
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_RandomUserApp)
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                AppNavigation()
            }
        }
    }
}
@Composable
private fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "overview") {
        composable("overview") {
            OverviewScreen(navController)
        }
        composable(
            "detail/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) { back ->
            val id = back.arguments!!.getString("userId")!!
            DetailScreen(userId = id, navController = navController)
        }
        composable(
            "edit/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) { back ->
            val id = back.arguments!!.getString("userId")!!
            EditUserScreen(navController = navController, userId = id)
        }
        composable("ar") {
            ArScreen(onUserClick = { user ->
                navController.navigate("detail/${user.id}")
            })
        }
    }
}