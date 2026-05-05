package me.edwarjimenez.gpsbgaconductor.ui.dashboard

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun DashboardScreen(
    onNavigateToMapa: () -> Unit,
    onNavigateToParadas: () -> Unit,
    onNavigateToPerfil: () -> Unit,
    onLogout: () -> Unit
) {
    Text(text = "Dashboard")
}