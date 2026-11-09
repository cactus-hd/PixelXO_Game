package ir.amir.pixelxo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ir.amir.pixelxo.ui.theme.PixelXOTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PixelXOTheme {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFF463FCD))
                ) { innerPadding ->
                    MyNav(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun MyNav(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    NavHost(
        navController = navController, startDestination = "mainpage", modifier = modifier,


    ) {

        composable("mainpage") {
            Mainpage(navController = navController)
        }

        composable(
            "gamepage/{player1Name}/{player2Name}",
            arguments = listOf(
                navArgument("player1Name"){type= NavType.StringType},
                navArgument("player2Name"){type= NavType.StringType}
            )
        ) {
            val playerone = it.arguments?.getString("player1Name")
            val playertwo = it.arguments?.getString("player2Name")
            gamepage(
                playerone = playerone!!,
                playertwo = playertwo!!
            )
        }
    }
}