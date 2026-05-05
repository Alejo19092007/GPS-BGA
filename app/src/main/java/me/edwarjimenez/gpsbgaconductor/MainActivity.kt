package me.edwarjimenez.gpsbgaconductor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import me.edwarjimenez.gpsbgaconductor.ui.auth.LoginScreen
import me.edwarjimenez.gpsbgaconductor.ui.auth.RegistroScreen
import me.edwarjimenez.gpsbgaconductor.ui.auth.RecuperarScreen
import me.edwarjimenez.gpsbgaconductor.ui.dashboard.DashboardScreen
import me.edwarjimenez.gpsbgaconductor.ui.map.MapaScreen
import me.edwarjimenez.gpsbgaconductor.ui.stops.ParadasScreen
import me.edwarjimenez.gpsbgaconductor.ui.profile.PerfilScreen
import me.edwarjimenez.gpsbgaconductor.ui.theme.GpsBGAConductorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GpsBGAConductorTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = "login",
                    modifier = Modifier.fillMaxSize()
                ) {
                    composable("login") {
                        LoginScreen(
                            onLoginSuccess = {
                                navController.navigate("dashboard") {
                                    popUpTo("login") { inclusive = true }
                                }
                            },
                            onNavigateToRegistro = {
                                navController.navigate("registro")
                            },
                            onNavigateToRecuperar = {
                                navController.navigate("recuperar")
                            }
                        )
                    }
                    composable("registro") {
                        RegistroScreen(
                            onRegistroSuccess = {
                                navController.navigate("dashboard") {
                                    popUpTo("login") { inclusive = true }
                                }
                            },
                            onBackClick = { navController.popBackStack() }
                        )
                    }
                    composable("recuperar") {
                        RecuperarScreen(
                            onBackClick = { navController.popBackStack() }
                        )
                    }
                    composable("dashboard") {
                        DashboardScreen(
                            onNavigateToMapa = { codigo ->
                                navController.navigate("mapa/$codigo")
                            },
                            onNavigateToParadas = { navController.navigate("paradas") },
                            onNavigateToPerfil = { navController.navigate("perfil") },
                            onLogout = {
                                navController.navigate("login") {
                                    popUpTo(0) { inclusive = true }
                                }
                            }
                        )
                    }
                    composable("mapa/{rutaCodigo}") { backStackEntry ->
                        MapaScreen(
                            onBackClick = { navController.popBackStack() },
                            rutaCodigo = backStackEntry.arguments?.getString("rutaCodigo") ?: "36"
                        )
                    }
                    composable("paradas") {
                        ParadasScreen(onBackClick = { navController.popBackStack() })
                    }
                    composable("perfil") {
                        PerfilScreen(
                            onBackClick = { navController.popBackStack() },
                            onLogout = {
                                navController.navigate("login") {
                                    popUpTo(0) { inclusive = true }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}