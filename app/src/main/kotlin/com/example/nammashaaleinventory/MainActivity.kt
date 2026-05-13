package com.example.nammashaaleinventory

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.nammashaaleinventory.data.Asset
import com.example.nammashaaleinventory.data.AssetCondition
import com.example.nammashaaleinventory.data.DashboardStats
import com.example.nammashaaleinventory.data.Repair
import com.example.nammashaaleinventory.data.RepairPriority
import com.example.nammashaaleinventory.data.RepairStatus
import com.example.nammashaaleinventory.ui.AppViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import java.time.LocalDate

class MainActivity : ComponentActivity() {
    private val viewModel: AppViewModel by viewModels {
        AppViewModel.Factory((application as NammaShaaleApp).repository)
    }
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var googleSignInLauncher: ActivityResultLauncher<Intent>
    private var googleAuthError by mutableStateOf<String?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        configureGoogleSignIn()
        setContent {
            NammaShaaleTheme {
                InventoryApp(
                    viewModel = viewModel,
                    googleAuthError = googleAuthError,
                    onGoogleSignIn = ::startGoogleSignIn,
                    onLogout = ::logoutFromFirebase
                )
            }
        }
    }

    private fun configureGoogleSignIn() {
        googleSignInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                val idToken = account.idToken
                if (idToken.isNullOrBlank()) {
                    googleAuthError = "Google token missing. Check Firebase Web Client ID."
                    return@registerForActivityResult
                }

                val credential = GoogleAuthProvider.getCredential(idToken, null)
                FirebaseAuth.getInstance().signInWithCredential(credential)
                    .addOnSuccessListener { authResult ->
                        val user = authResult.user
                        viewModel.loginWithFirebaseUser(user?.email ?: user?.displayName ?: "Google User")
                        googleAuthError = null
                    }
                    .addOnFailureListener { exception ->
                        googleAuthError = exception.localizedMessage ?: "Google sign-in failed."
                    }
            } catch (exception: ApiException) {
                googleAuthError = "Google sign-in failed: ${exception.statusCode}"
            }
        }

        val webClientId = getString(R.string.google_web_client_id)
        val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(webClientId)
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, options)
    }

    private fun startGoogleSignIn() {
        val webClientId = getString(R.string.google_web_client_id)
        if (webClientId.isBlank() || webClientId == "REPLACE_WITH_FIREBASE_WEB_CLIENT_ID") {
            googleAuthError = "Add Firebase Web Client ID in app/src/main/res/values/strings.xml."
            return
        }
        googleAuthError = null
        googleSignInLauncher.launch(googleSignInClient.signInIntent)
    }

    private fun logoutFromFirebase() {
        FirebaseAuth.getInstance().signOut()
        googleSignInClient.signOut()
        viewModel.firebaseLogout()
    }
}

private sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    data object Dashboard : Screen("dashboard", "Home", Icons.Default.Home)
    data object AddAsset : Screen("addAsset", "Add", Icons.Default.Add)
    data object Assets : Screen("assets", "Assets", Icons.Default.Inventory2)
    data object Condition : Screen("condition", "Health", Icons.Default.CheckCircle)
    data object Issues : Screen("issues", "Issues", Icons.Default.ReportProblem)
    data object Repairs : Screen("repairs", "Repairs", Icons.Default.Build)
    data object Reports : Screen("reports", "Reports", Icons.Default.Assessment)
    data object Profile : Screen("profile", "Profile", Icons.Default.Person)
}

private val mainScreens = listOf(
    Screen.Dashboard,
    Screen.Assets,
    Screen.Repairs,
    Screen.Reports,
    Screen.Profile
)

@Composable
private fun NammaShaaleTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = MaterialTheme.colorScheme.copy(
            primary = Color(0xFF1565C0),
            secondary = Color(0xFF42A5F5),
            tertiary = Color(0xFFF59E0B),
            background = Color(0xFFF5F7FA),
            surface = Color.White,
            error = Color(0xFFB91C1C)
        ),
        content = content
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun InventoryApp(
    viewModel: AppViewModel,
    googleAuthError: String?,
    onGoogleSignIn: () -> Unit,
    onLogout: () -> Unit
) {
    val navController = rememberNavController()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val session by viewModel.sessionState.collectAsState()
    val isLoggedIn = session.first
    val email = session.second
    var fabExpanded by remember { mutableStateOf(false) }

    if (!isLoggedIn) {
        LoginScreen(
            onLogin = viewModel::login,
            onGoogleSignIn = onGoogleSignIn,
            googleAuthError = googleAuthError
        )
        return
    }

    Scaffold(
        containerColor = Color(0xFFF5F7FA),
        bottomBar = { InventoryBottomBar(navController) },
        floatingActionButton = {
            DashboardFab(
                expanded = fabExpanded,
                onExpandedChange = { fabExpanded = it },
                onAddAsset = {
                    fabExpanded = false
                    navController.navigate(Screen.AddAsset.route) { launchSingleTop = true }
                },
                onReportIssue = {
                    fabExpanded = false
                    navController.navigate(Screen.Issues.route) { launchSingleTop = true }
                }
            )
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Dashboard.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(Screen.Dashboard.route) {
                DashboardScreen(uiState.stats, uiState.assets, navController)
            }
            composable(Screen.AddAsset.route) {
                AddAssetScreen(
                    onSave = viewModel::addAsset,
                    onSaved = { navController.navigate(Screen.Assets.route) { launchSingleTop = true } }
                )
            }
            composable(Screen.Assets.route) {
                AssetListScreen(uiState.assets)
            }
            composable(Screen.Condition.route) {
                ConditionUpdateScreen(uiState.assets, viewModel::updateConditions)
            }
            composable(Screen.Issues.route) {
                IssueReportScreen(uiState.assets, viewModel::reportIssue)
            }
            composable(Screen.Repairs.route) {
                RepairRequestScreen(uiState.repairs, uiState.assets, viewModel::updateRepair)
            }
            composable(Screen.Reports.route) {
                ReportsScreen(uiState.stats, uiState.assets)
            }
            composable(Screen.Profile.route) {
                ProfileScreen(email, onLogout)
            }
        }
    }
}

@Composable
private fun LoginScreen(
    onLogin: (String, String) -> String?,
    onGoogleSignIn: () -> Unit,
    googleAuthError: String?
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    Surface(Modifier.fillMaxSize(), color = Color(0xFFF8FAFC)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text("Namma-Shaale Inventory", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text("Digital Asset Auditor", color = Color(0xFF475569), modifier = Modifier.padding(top = 4.dp, bottom = 28.dp))
            OutlinedTextField(email, { email = it }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(
                password,
                { password = it },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )
            if (error != null) {
                Text(error.orEmpty(), color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(top = 10.dp))
            }
            Button(
                onClick = { error = onLogin(email, password) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 18.dp)
            ) {
                Text("Login")
            }
            OutlinedButton(
                onClick = onGoogleSignIn,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Continue with Google")
            }
            if (googleAuthError != null) {
                Text(googleAuthError, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(top = 10.dp))
            }
            Text("Forgot password?", color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(top = 14.dp))
        }
    }
}

@Composable
private fun InventoryBottomBar(navController: NavController) {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    NavigationBar(
        modifier = Modifier
            .height(70.dp)
            .shadow(8.dp, RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)),
        containerColor = Color.White,
        tonalElevation = 8.dp
    ) {
        mainScreens.forEach { screen ->
            NavigationBarItem(
                selected = currentRoute == screen.route,
                onClick = { navController.navigate(screen.route) { launchSingleTop = true } },
                icon = {
                    Icon(
                        screen.icon,
                        contentDescription = screen.title,
                        modifier = Modifier.size(24.dp),
                        tint = if (currentRoute == screen.route) Color(0xFF1565C0) else Color(0xFF80868B)
                    )
                },
                label = {
                    Text(
                        screen.title,
                        fontSize = 12.sp,
                        color = if (currentRoute == screen.route) Color(0xFF1565C0) else Color(0xFF80868B)
                    )
                }
            )
        }
    }
}

@Composable
private fun DashboardFab(
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onAddAsset: () -> Unit,
    onReportIssue: () -> Unit
) {
    Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.spacedBy(10.dp)) {
        AnimatedVisibility(visible = expanded) {
            Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.spacedBy(10.dp)) {
                ExtendedFloatingActionButton(
                    onClick = onAddAsset,
                    icon = { Icon(Icons.Default.Add, contentDescription = "Add Asset") },
                    text = { Text("Add Asset") },
                    containerColor = Color.White,
                    contentColor = Color(0xFF1565C0)
                )
                ExtendedFloatingActionButton(
                    onClick = onReportIssue,
                    icon = { Icon(Icons.Default.Warning, contentDescription = "Report Issue") },
                    text = { Text("Report Issue") },
                    containerColor = Color.White,
                    contentColor = Color(0xFFF59E0B)
                )
            }
        }
        FloatingActionButton(
            onClick = { onExpandedChange(!expanded) },
            containerColor = Color(0xFF1565C0),
            contentColor = Color.White
        ) {
            Icon(if (expanded) Icons.Default.Close else Icons.Default.Add, contentDescription = "Quick actions")
        }
    }
}

@Composable
private fun DashboardScreen(stats: DashboardStats, assets: List<Asset>, navController: NavController) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedSection by remember { mutableStateOf("All Sections") }
    val visibleAssets = assets.filter { selectedSection == "All Sections" || it.section == selectedSection }
    val visibleStats = remember(visibleAssets) { visibleAssets.toDashboardStats() }
    val filteredAssets = assets.filter {
        (selectedSection == "All Sections" || it.section == selectedSection) &&
            (searchQuery.isBlank() ||
            it.assetName.contains(searchQuery, ignoreCase = true) ||
            it.serialNumber.contains(searchQuery, ignoreCase = true) ||
            it.category.contains(searchQuery, ignoreCase = true) ||
            it.section.contains(searchQuery, ignoreCase = true))
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F7FA)),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item { DashboardHeader(stats) }
        item { DashboardSearchBar(searchQuery) { searchQuery = it } }
        item {
            SectionFilterCard(
                selectedSection = selectedSection,
                sections = dashboardSections(assets),
                onSectionSelected = { selectedSection = it }
            )
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(14.dp), modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth()) {
                StatCard("Total Assets", visibleStats.totalAssets.toString(), Icons.Default.Inventory2, Color(0xFF1565C0), Modifier.weight(1f))
                StatCard("Working", visibleStats.workingItems.toString(), Icons.Default.CheckCircle, Color(0xFF16A34A), Modifier.weight(1f))
            }
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(14.dp), modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth()) {
                StatCard("Needs Repair", visibleStats.needsRepair.toString(), Icons.Default.Warning, Color(0xFFF59E0B), Modifier.weight(1f))
                StatCard("Broken", visibleStats.brokenItems.toString(), Icons.Default.Close, Color(0xFFDC2626), Modifier.weight(1f))
            }
        }
        item { SectionNeedCard(selectedSection, visibleStats) }
        item { AiSuggestionsCard(visibleStats, visibleAssets) }
        item { ConditionChart(visibleStats) }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth()) {
                QuickAction("Add Asset", Icons.Default.Add, Modifier.weight(1f)) { navController.navigate(Screen.AddAsset.route) }
                QuickAction("Report Issue", Icons.Default.ReportProblem, Modifier.weight(1f)) { navController.navigate(Screen.Issues.route) }
            }
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth()) {
                QuickAction("Health Check", Icons.Default.CheckCircle, Modifier.weight(1f)) { navController.navigate(Screen.Condition.route) }
                QuickAction("Repairs", Icons.Default.Build, Modifier.weight(1f)) { navController.navigate(Screen.Repairs.route) }
            }
        }
        item {
            SectionTitle("Recent Assets", Modifier.padding(horizontal = 16.dp))
        }
        if (filteredAssets.isEmpty()) {
            item { EmptyAssetsState(navController) }
        } else {
            items(filteredAssets.take(5)) { asset ->
                AssetRow(asset, modifier = Modifier.padding(horizontal = 16.dp))
            }
        }
        item { Spacer(Modifier.height(72.dp)) }
    }
}

@Composable
private fun DashboardHeader(stats: DashboardStats) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp))
            .background(
                Brush.linearGradient(
                    listOf(Color(0xFF42A5F5), Color(0xFF1565C0))
                )
            )
            .padding(start = 20.dp, end = 20.dp, top = 24.dp, bottom = 24.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text("Good Morning", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
            Text("Govt High School Inventory Portal", color = Color(0xFFEAF4FF), fontSize = 16.sp)
            Text("Last Updated: Today, 10:30 AM", color = Color(0xFFD7EAFF), fontSize = 14.sp, modifier = Modifier.padding(top = 8.dp))
        }
        Row(
            modifier = Modifier.align(Alignment.TopEnd),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(contentAlignment = Alignment.TopEnd) {
                Icon(
                    Icons.Default.Notifications,
                    contentDescription = "Notifications",
                    tint = Color.White,
                    modifier = Modifier
                        .size(42.dp)
                        .background(Color.White.copy(alpha = 0.18f), CircleShape)
                        .padding(9.dp)
                )
                if (stats.needsRepair > 0) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .background(Color(0xFFFFD166), CircleShape)
                    )
                }
            }
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .background(Color.White.copy(alpha = 0.18f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Person, contentDescription = "Profile", tint = Color.White, modifier = Modifier.size(24.dp))
            }
        }
        Text(
            "${stats.needsRepair} Pending Repairs",
            color = Color.White,
            fontSize = 13.sp,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .background(Color.White.copy(alpha = 0.16f), RoundedCornerShape(16.dp))
                .padding(horizontal = 10.dp, vertical = 6.dp)
        )
    }
}

@Composable
private fun DashboardSearchBar(value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
        label = { Text("Search assets, equipment...") },
        singleLine = true,
        shape = RoundedCornerShape(24.dp),
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .background(Color(0xFFECEFF3), RoundedCornerShape(24.dp))
    )
}

@Composable
private fun SectionFilterCard(
    selectedSection: String,
    sections: List<String>,
    onSectionSelected: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text("Section Wise Inventory", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            DropdownField("Select class / section", selectedSection, sections, Modifier.fillMaxWidth(), onSectionSelected)
            Text("Admin can check what is broken, missing, or needs repair in this section.", color = Color(0xFF64748B), fontSize = 13.sp)
        }
    }
}

@Composable
private fun SectionNeedCard(selectedSection: String, stats: DashboardStats) {
    val title = if (selectedSection == "All Sections") "All Sections Requirement Summary" else "$selectedSection Requirement Summary"
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(title, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text("Broken: ${stats.brokenItems}", color = Color(0xFFDC2626), fontSize = 14.sp)
            Text("Needs Repair: ${stats.needsRepair}", color = Color(0xFFF59E0B), fontSize = 14.sp)
            Text("Missing: ${stats.missingItems}", color = Color(0xFF64748B), fontSize = 14.sp)
        }
    }
}

@Composable
private fun StatCard(label: String, value: String, icon: ImageVector, color: Color, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .height(124.dp)
            .shadow(4.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.SpaceBetween) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(color.copy(alpha = 0.12f), RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = label, tint = color, modifier = Modifier.size(22.dp))
            }
            Column {
                Text(value, color = color, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Text(label, color = Color(0xFF64748B), fontSize = 14.sp)
            }
        }
    }
}

@Composable
private fun QuickAction(label: String, icon: ImageVector, modifier: Modifier = Modifier, onClick: () -> Unit) {
    OutlinedButton(onClick = onClick, modifier = modifier.height(56.dp), shape = RoundedCornerShape(16.dp)) {
        Icon(icon, contentDescription = label, modifier = Modifier.size(22.dp))
        Spacer(Modifier.width(8.dp))
        Text(label, fontSize = 14.sp)
    }
}

@Composable
private fun AiSuggestionsCard(stats: DashboardStats, assets: List<Asset>) {
    val uncheckedCount = assets.count { it.condition != AssetCondition.Working }.coerceAtLeast(stats.needsRepair)
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    Modifier.size(38.dp).background(Color(0xFFE3F2FD), RoundedCornerShape(10.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.SmartToy, contentDescription = "AI Suggestions", tint = Color(0xFF1565C0))
                }
                Spacer(Modifier.width(10.dp))
                Text("AI Suggestions", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
            SuggestionLine("Microscope in Lab 2 may require maintenance soon.")
            SuggestionLine("$uncheckedCount assets have not been checked this month.")
        }
    }
}

@Composable
private fun SuggestionLine(text: String) {
    Row(verticalAlignment = Alignment.Top) {
        Icon(Icons.Default.Warning, contentDescription = null, tint = Color(0xFFF59E0B), modifier = Modifier.size(18.dp))
        Spacer(Modifier.width(8.dp))
        Text(text, color = Color(0xFF475569), fontSize = 14.sp)
    }
}

@Composable
private fun ConditionChart(stats: DashboardStats) {
    val total = stats.totalAssets.coerceAtLeast(1).toFloat()
    val slices = listOf(
        stats.workingItems to Color(0xFF16A34A),
        stats.needsRepair to Color(0xFFF59E0B),
        stats.brokenItems to Color(0xFFDC2626),
        stats.missingItems to Color(0xFF64748B)
    )
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(22.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(Modifier.size(146.dp), contentAlignment = Alignment.Center) {
                Canvas(Modifier.fillMaxSize()) {
                    var startAngle = -90f
                    slices.forEach { (count, color) ->
                        val sweep = (count / total) * 360f
                        drawArc(color, startAngle, sweep, false, style = Stroke(22.dp.toPx(), cap = StrokeCap.Butt))
                        startAngle += sweep
                    }
                    if (stats.totalAssets == 0) {
                        drawCircle(Color(0xFFE2E8F0), style = Stroke(22.dp.toPx()))
                    }
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(stats.totalAssets.toString(), fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1565C0))
                    Text("Total", fontSize = 12.sp, color = Color(0xFF64748B))
                }
            }
            Spacer(Modifier.width(18.dp))
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("Asset Health", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Legend("Working", stats.workingItems, total, Color(0xFF16A34A))
                Legend("Repair", stats.needsRepair, total, Color(0xFFF59E0B))
                Legend("Broken", stats.brokenItems, total, Color(0xFFDC2626))
                Legend("Missing", stats.missingItems, total, Color(0xFF64748B))
            }
        }
    }
}

@Composable
private fun Legend(label: String, count: Int, total: Float, color: Color) {
    val percent = if (total <= 0f) 0 else ((count / total) * 100).toInt()
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(Modifier.size(10.dp).background(color, RoundedCornerShape(2.dp)))
        Spacer(Modifier.width(8.dp))
        Text("$percent% $label", fontSize = 13.sp, color = Color(0xFF475569))
    }
}

@Composable
private fun AddAssetScreen(
    onSave: (String, String, String, String, String, String, AssetCondition) -> Unit,
    onSaved: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var serial by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Sports") }
    var section by remember { mutableStateOf("Class 6 - Sec A") }
    var condition by remember { mutableStateOf(AssetCondition.Working.name) }
    var purchaseDate by remember { mutableStateOf(LocalDate.now().toString()) }
    var message by remember { mutableStateOf("") }

    FormPage("Register Asset") {
        OutlinedTextField(name, { name = it }, label = { Text("Asset name") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(serial, { serial = it }, label = { Text("Serial number") }, modifier = Modifier.fillMaxWidth())
        DropdownField("Category", category, listOf("Sports", "Lab", "Tablet", "Classroom", "Library")) { category = it }
        DropdownField("Class / Section", section, assetSections()) { section = it }
        DropdownField("Condition", condition, AssetCondition.entries.map { it.name }) { condition = it }
        OutlinedTextField(purchaseDate, { purchaseDate = it }, label = { Text("Purchase date") }, modifier = Modifier.fillMaxWidth())
        OutlinedButton(onClick = { message = "Photo picker placeholder selected." }, shape = RoundedCornerShape(8.dp)) {
            Icon(Icons.Default.CameraAlt, contentDescription = "Asset photo")
            Spacer(Modifier.width(8.dp))
            Text("Asset Photo")
        }
        Button(
            onClick = {
                if (name.isNotBlank() && serial.isNotBlank()) {
                    onSave(name, serial, category, section, purchaseDate, "", AssetCondition.valueOf(condition))
                    name = ""
                    serial = ""
                    section = "Class 6 - Sec A"
                    condition = AssetCondition.Working.name
                    message = "Asset saved."
                    onSaved()
                } else {
                    message = "Asset name and serial number are required."
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Save Asset") }
        OutlinedButton(onClick = {
            name = ""
            serial = ""
            message = "Form cleared."
        }, modifier = Modifier.fillMaxWidth()) { Text("Cancel") }
        if (message.isNotBlank()) Text(message, color = MaterialTheme.colorScheme.primary)
    }
}

@Composable
private fun AssetListScreen(assets: List<Asset>) {
    var query by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("All") }
    var section by remember { mutableStateOf("All Sections") }
    var condition by remember { mutableStateOf("All") }
    val filtered = assets.filter {
        (query.isBlank() || it.assetName.contains(query, true) || it.serialNumber.contains(query, true) || it.section.contains(query, true)) &&
            (category == "All" || it.category == category) &&
            (section == "All Sections" || it.section == section) &&
            (condition == "All" || it.condition.name == condition)
    }
    val sectionStats = remember(filtered) { filtered.toDashboardStats() }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item { SectionTitle("Asset List") }
        item {
            OutlinedTextField(
                query,
                { query = it },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                label = { Text("Search assets") },
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                DropdownField("Category", category, listOf("All", "Sports", "Lab", "Tablet", "Classroom", "Library"), Modifier.weight(1f)) { category = it }
                DropdownField("Condition", condition, listOf("All") + AssetCondition.entries.map { it.name }, Modifier.weight(1f)) { condition = it }
            }
        }
        item {
            DropdownField("Class / Section", section, dashboardSections(assets), Modifier.fillMaxWidth()) { section = it }
        }
        item {
            SectionNeedCard(section, sectionStats)
        }
        items(filtered) { AssetRow(it) }
    }
}

@Composable
private fun ConditionUpdateScreen(assets: List<Asset>, onSave: (List<Long>, AssetCondition) -> Unit) {
    val selected = remember { mutableStateListOf<Long>() }
    var condition by remember { mutableStateOf(AssetCondition.Working.name) }
    var message by remember { mutableStateOf("") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item { SectionTitle("Monthly Health Check") }
        item {
            DropdownField("New status", condition, AssetCondition.entries.map { it.name }) { condition = it }
        }
        items(assets) { asset ->
            Card(shape = RoundedCornerShape(8.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .toggleable(
                            value = selected.contains(asset.assetId),
                            onValueChange = {
                                if (it) selected.add(asset.assetId) else selected.remove(asset.assetId)
                            }
                        )
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(selected.contains(asset.assetId), null)
                    Spacer(Modifier.width(8.dp))
                    Column {
                        Text(asset.assetName, fontWeight = FontWeight.SemiBold)
                        Text(asset.condition.name, color = Color(0xFF64748B))
                    }
                }
            }
        }
        item {
            Button(
                onClick = {
                    onSave(selected.toList(), AssetCondition.valueOf(condition))
                    selected.clear()
                    message = "Health check updated."
                },
                modifier = Modifier.fillMaxWidth()
            ) { Text("Save Updates") }
            if (message.isNotBlank()) Text(message, color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(top = 8.dp))
        }
    }
}

@Composable
private fun IssueReportScreen(assets: List<Asset>, onReport: (Long, String, String, String) -> Unit) {
    var selectedAsset by remember(assets) { mutableStateOf(assets.firstOrNull()?.assetId ?: 0L) }
    var description by remember { mutableStateOf("") }
    var issueDate by remember { mutableStateOf(LocalDate.now().toString()) }
    var message by remember { mutableStateOf("") }

    LaunchedEffect(assets) {
        if (selectedAsset == 0L && assets.isNotEmpty()) selectedAsset = assets.first().assetId
    }

    FormPage("Report Issue") {
        DropdownField(
            label = "Asset",
            value = assets.firstOrNull { it.assetId == selectedAsset }?.assetName ?: "No assets",
            options = assets.map { it.assetName }.ifEmpty { listOf("No assets") }
        ) { assetName -> selectedAsset = assets.firstOrNull { it.assetName == assetName }?.assetId ?: 0L }
        OutlinedTextField(
            description,
            { description = it },
            label = { Text("Issue description") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3
        )
        OutlinedTextField(issueDate, { issueDate = it }, label = { Text("Issue date") }, modifier = Modifier.fillMaxWidth())
        OutlinedButton(onClick = { message = "Image upload placeholder selected." }, shape = RoundedCornerShape(8.dp)) {
            Icon(Icons.Default.CameraAlt, contentDescription = "Issue image")
            Spacer(Modifier.width(8.dp))
            Text("Upload Image")
        }
        Button(
            onClick = {
                if (selectedAsset != 0L && description.isNotBlank()) {
                    onReport(selectedAsset, description, issueDate, "")
                    description = ""
                    message = "Issue reported and repair request created."
                } else {
                    message = "Select an asset and enter the issue description."
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Submit Issue") }
        if (message.isNotBlank()) Text(message, color = MaterialTheme.colorScheme.primary)
    }
}

@Composable
private fun RepairRequestScreen(
    repairs: List<Repair>,
    assets: List<Asset>,
    onUpdate: (Repair, RepairPriority?, RepairStatus?) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item { SectionTitle("Repair Requests") }
        items(repairs) { repair ->
            val assetName = assets.firstOrNull { it.assetId == repair.assetId }?.assetName ?: "Asset #${repair.assetId}"
            Card(shape = RoundedCornerShape(8.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(assetName, fontWeight = FontWeight.SemiBold)
                    Text("Assigned to ${repair.assignedTo}", color = Color(0xFF64748B))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                        DropdownField("Priority", repair.priority.name, RepairPriority.entries.map { it.name }, Modifier.weight(1f)) {
                            onUpdate(repair, RepairPriority.valueOf(it), null)
                        }
                        DropdownField("Status", repair.repairStatus.name, RepairStatus.entries.map { it.name }, Modifier.weight(1f)) {
                            onUpdate(repair, null, RepairStatus.valueOf(it))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ReportsScreen(stats: DashboardStats, assets: List<Asset>) {
    FormPage("Reports") {
        Text("Monthly Inventory Summary", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        HorizontalDivider()
        Text("Total assets: ${stats.totalAssets}")
        Text("Working items: ${stats.workingItems}")
        Text("Needs repair: ${stats.needsRepair}")
        Text("Broken items: ${stats.brokenItems}")
        Text("Missing items: ${stats.missingItems}")
        HorizontalDivider()
        assets.groupBy { it.category }.forEach { (category, items) ->
            Text("$category: ${items.size}")
        }
        OutlinedButton(onClick = { }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(8.dp)) {
            Icon(Icons.Default.Assessment, contentDescription = "Export PDF")
            Spacer(Modifier.width(8.dp))
            Text("Export PDF")
        }
    }
}

@Composable
private fun ProfileScreen(email: String, onLogout: () -> Unit) {
    FormPage("Profile") {
        Text("User", style = MaterialTheme.typography.labelLarge, color = Color(0xFF64748B))
        Text(email, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
        HorizontalDivider()
        Text("Role: Teacher / Administrator")
        Text("Cloud database: Firebase Firestore")
        Text("Authentication: Ready for Firebase configuration")
        Button(onClick = onLogout, modifier = Modifier.fillMaxWidth()) {
            Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = "Logout")
            Spacer(Modifier.width(8.dp))
            Text("Logout")
        }
    }
}

@Composable
private fun EmptyAssetsState(navController: NavController) {
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .clickable { navController.navigate(Screen.AddAsset.route) },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(Modifier.size(54.dp).background(Color(0xFFE3F2FD), CircleShape), contentAlignment = Alignment.Center) {
                Icon(Icons.Default.Inventory2, contentDescription = null, tint = Color(0xFF1565C0), modifier = Modifier.size(28.dp))
            }
            Text("No assets added yet.", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text("Tap + to add your first asset.", color = Color(0xFF64748B), fontSize = 14.sp)
        }
    }
}

@Composable
private fun AssetRow(asset: Asset, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(3.dp, RoundedCornerShape(16.dp))
            .clickable { },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(48.dp).background(categoryColor(asset.category).copy(alpha = 0.14f), RoundedCornerShape(12.dp)), contentAlignment = Alignment.Center) {
                Icon(categoryIcon(asset.category), contentDescription = null, tint = categoryColor(asset.category), modifier = Modifier.size(24.dp))
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(asset.assetName, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                Text("Category: ${asset.category}", color = Color(0xFF64748B), fontSize = 13.sp)
                Text("Section: ${asset.section}", color = Color(0xFF64748B), fontSize = 13.sp)
                Text("Last Checked: Today", color = Color(0xFF94A3B8), fontSize = 12.sp)
            }
            StatusBadge(asset.condition)
        }
    }
}

@Composable
private fun FormPage(title: String, content: @Composable ColumnScope.() -> Unit) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            SectionTitle(title)
        }
        item {
            Card(shape = RoundedCornerShape(8.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    content = content
                )
            }
        }
    }
}

@Composable
private fun SectionTitle(title: String, modifier: Modifier = Modifier) {
    Text(title, modifier = modifier, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DropdownField(
    label: String,
    value: String,
    options: List<String>,
    modifier: Modifier = Modifier,
    onSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }, modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        expanded = false
                        onSelected(option)
                    }
                )
            }
        }
    }
}

private fun conditionColor(condition: AssetCondition): Color = when (condition) {
    AssetCondition.Working -> Color(0xFF16A34A)
    AssetCondition.NeedsRepair -> Color(0xFFF59E0B)
    AssetCondition.Broken -> Color(0xFFDC2626)
    AssetCondition.Missing -> Color(0xFF64748B)
}

@Composable
private fun StatusBadge(condition: AssetCondition) {
    val color = conditionColor(condition)
    Text(
        condition.name,
        color = color,
        fontSize = 12.sp,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier
            .background(color.copy(alpha = 0.12f), RoundedCornerShape(14.dp))
            .padding(horizontal = 10.dp, vertical = 6.dp)
    )
}

private fun categoryIcon(category: String): ImageVector = when (category.lowercase()) {
    "sports" -> Icons.Default.CheckCircle
    "lab" -> Icons.Default.SmartToy
    "tablet" -> Icons.Default.Assessment
    "classroom", "furniture" -> Icons.Default.Inventory2
    else -> Icons.Default.Inventory2
}

private fun categoryColor(category: String): Color = when (category.lowercase()) {
    "sports" -> Color(0xFF16A34A)
    "lab" -> Color(0xFF7C3AED)
    "tablet" -> Color(0xFF1565C0)
    "classroom", "furniture" -> Color(0xFFF59E0B)
    else -> Color(0xFF64748B)
}

private fun assetSections(): List<String> = listOf(
    "Class 6 - Sec A",
    "Class 6 - Sec B",
    "Class 6 - Sec C",
    "Class 7 - Sec A",
    "Class 7 - Sec B",
    "Class 7 - Sec C",
    "Class 8 - Sec A",
    "Class 8 - Sec B",
    "Class 8 - Sec C",
    "Lab 1",
    "Lab 2",
    "Library",
    "Sports Room",
    "Office",
    "Unassigned"
)

private fun dashboardSections(assets: List<Asset>): List<String> =
    listOf("All Sections") + (assetSections() + assets.map { it.section.ifBlank { "Unassigned" } })
        .distinct()
        .filterNot { it == "All Sections" }

private fun List<Asset>.toDashboardStats(): DashboardStats = DashboardStats(
    totalAssets = size,
    workingItems = count { it.condition == AssetCondition.Working },
    brokenItems = count { it.condition == AssetCondition.Broken },
    needsRepair = count { it.condition == AssetCondition.NeedsRepair },
    missingItems = count { it.condition == AssetCondition.Missing }
)
