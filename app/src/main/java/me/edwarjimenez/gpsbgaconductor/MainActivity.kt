package me.edwarjimenez.gpsbgaconductor

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import me.edwarjimenez.gpsbgaconductor.ui.auth.LoginScreen
import me.edwarjimenez.gpsbgaconductor.ui.auth.RegistroScreen
import me.edwarjimenez.gpsbgaconductor.ui.auth.RecuperarScreen
import me.edwarjimenez.gpsbgaconductor.ui.dashboard.DashboardScreen
import me.edwarjimenez.gpsbgaconductor.ui.map.MapaScreen
import me.edwarjimenez.gpsbgaconductor.ui.stops.ParadasScreen
import me.edwarjimenez.gpsbgaconductor.ui.profile.PerfilScreen
import me.edwarjimenez.gpsbgaconductor.ui.profile.EditarPerfilScreen
import me.edwarjimenez.gpsbgaconductor.ui.profile.MisRutasScreen
import me.edwarjimenez.gpsbgaconductor.ui.profile.HistorialScreen
import me.edwarjimenez.gpsbgaconductor.ui.profile.AyudaScreen
import me.edwarjimenez.gpsbgaconductor.ui.theme.GpsBGAConductorTheme
import androidx.compose.ui.graphics.Color

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GpsBGAConductorTheme {
                val context = LocalContext.current
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                val rutasSinNavBar = listOf("login", "registro", "recuperar")
                val mostrarNavBar = currentRoute !in rutasSinNavBar &&
                        !currentRoute.orEmpty().startsWith("mapa") &&
                        currentRoute != "editar_perfil" &&
                        currentRoute != "mis_rutas" &&
                        currentRoute != "historial" &&
                        currentRoute != "ayuda"

                val bgDark = Color(0xFF0A0F1E)
                val bgCard = Color(0xFF0D1830)
                val bluePrimary = Color(0xFF0078FF)
                val blueMuted = Color(0xFF4A7FC0)
                val cyanPrimary = Color(0xFF00C6FF)
                val blueBorder = Color(0xFF1E2D5A)

                Scaffold(
                    bottomBar = {
                        if (mostrarNavBar) {
                            NavigationBar(
                                containerColor = bgCard,
                                tonalElevation = 0.dp
                            ) {
                                NavigationBarItem(
                                    selected = currentRoute == "dashboard",
                                    onClick = {
                                        navController.navigate("dashboard") {
                                            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    },
                                    icon = { Icon(Icons.Default.Home, contentDescription = null) },
                                    label = { Text("Inicio", fontSize = 10.sp) },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = cyanPrimary,
                                        selectedTextColor = cyanPrimary,
                                        unselectedIconColor = blueMuted,
                                        unselectedTextColor = blueMuted,
                                        indicatorColor = bluePrimary.copy(alpha = 0.2f)
                                    )
                                )
                                NavigationBarItem(
                                    selected = currentRoute?.startsWith("mapa") == true,
                                    onClick = {
                                        val prefs = context.getSharedPreferences("gpsbga_prefs", Context.MODE_PRIVATE)
                                        val rutaActiva = prefs.getString("ruta_codigo", "36") ?: "36"
                                        navController.navigate("mapa/$rutaActiva") {
                                            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    },
                                    icon = { Icon(Icons.Default.Map, contentDescription = null) },
                                    label = { Text("Mapa", fontSize = 10.sp) },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = cyanPrimary,
                                        selectedTextColor = cyanPrimary,
                                        unselectedIconColor = blueMuted,
                                        unselectedTextColor = blueMuted,
                                        indicatorColor = bluePrimary.copy(alpha = 0.2f)
                                    )
                                )
                                NavigationBarItem(
                                    selected = currentRoute?.startsWith("paradas") == true,
                                    onClick = {
                                        val prefs = context.getSharedPreferences("gpsbga_prefs", Context.MODE_PRIVATE)
                                        val rutaActiva = prefs.getString("ruta_codigo", "36") ?: "36"
                                        navController.navigate("paradas/$rutaActiva") {
                                            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    },
                                    icon = { Icon(Icons.Default.LocationOn, contentDescription = null) },
                                    label = { Text("Paradas", fontSize = 10.sp) },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = cyanPrimary,
                                        selectedTextColor = cyanPrimary,
                                        unselectedIconColor = blueMuted,
                                        unselectedTextColor = blueMuted,
                                        indicatorColor = bluePrimary.copy(alpha = 0.2f)
                                    )
                                )
                                NavigationBarItem(
                                    selected = currentRoute == "perfil",
                                    onClick = {
                                        navController.navigate("perfil") {
                                            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    },
                                    icon = { Icon(Icons.Default.Person, contentDescription = null) },
                                    label = { Text("Perfil", fontSize = 10.sp) },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = cyanPrimary,
                                        selectedTextColor = cyanPrimary,
                                        unselectedIconColor = blueMuted,
                                        unselectedTextColor = blueMuted,
                                        indicatorColor = bluePrimary.copy(alpha = 0.2f)
                                    )
                                )
                            }
                        }
                    },
                    containerColor = bgDark
                ) { paddingValues ->
                    NavHost(
                        navController = navController,
                        startDestination = "login",
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                    ) {
                        composable("login") {
                            LoginScreen(
                                onLoginSuccess = {
                                    navController.navigate("dashboard") {
                                        popUpTo("login") { inclusive = true }
                                    }
                                },
                                onNavigateToRegistro = { navController.navigate("registro") },
                                onNavigateToRecuperar = { navController.navigate("recuperar") }
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
                            RecuperarScreen(onBackClick = { navController.popBackStack() })
                        }
                        composable("dashboard") {
                            DashboardScreen(
                                onNavigateToMapa = { navController.navigate("mapa/$it") },
                                onNavigateToParadas = { navController.navigate("paradas/$it") },
                                onNavigateToPerfil = { navController.navigate("perfil") },
                                onLogout = {
                                    navController.navigate("login") {
                                        popUpTo(0) { inclusive = true }
                                    }
                                }
                            )
                        }
                        composable("mapa/{rutaCodigo}") { backStackEntry ->
                            val codigo = backStackEntry.arguments?.getString("rutaCodigo") ?: "36"
                            MapaScreen(
                                onBackClick = { navController.popBackStack() },
                                onNavigateToParadas = { navController.navigate("paradas/$it") },
                                rutaCodigo = codigo
                            )
                        }
                        composable("paradas/{rutaCodigo}") { backStackEntry ->
                            ParadasScreen(
                                onBackClick = { navController.popBackStack() },
                                rutaCodigo = backStackEntry.arguments?.getString("rutaCodigo") ?: "36"
                            )
                        }
                        composable("perfil") {
                            PerfilScreen(
                                onBackClick = { navController.popBackStack() },
                                onLogout = {
                                    navController.navigate("login") {
                                        popUpTo(0) { inclusive = true }
                                    }
                                },
                                onEditarPerfil = { navController.navigate("editar_perfil") },
                                onMisRutas = { navController.navigate("mis_rutas") },
                                onHistorial = { navController.navigate("historial") },
                                onAyuda = { navController.navigate("ayuda") }
                            )
                        }
                        composable("editar_perfil") {
                            EditarPerfilScreen(onBackClick = { navController.popBackStack() })
                        }
                        composable("mis_rutas") {
                            MisRutasScreen(onBackClick = { navController.popBackStack() })
                        }
                        composable("historial") {
                            HistorialScreen(onBackClick = { navController.popBackStack() })
                        }
                        composable("ayuda") {
                            AyudaScreen(onBackClick = { navController.popBackStack() })
                        }
                    }
                }
            }
        }
    }
}