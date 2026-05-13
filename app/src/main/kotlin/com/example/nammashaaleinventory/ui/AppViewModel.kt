package com.example.nammashaaleinventory.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.example.nammashaaleinventory.data.Asset
import com.example.nammashaaleinventory.data.AssetCondition
import com.example.nammashaaleinventory.data.DashboardStats
import com.example.nammashaaleinventory.data.InventoryRepository
import com.example.nammashaaleinventory.data.Issue
import com.example.nammashaaleinventory.data.Repair
import com.example.nammashaaleinventory.data.RepairPriority
import com.example.nammashaaleinventory.data.RepairStatus
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class InventoryUiState(
    val assets: List<Asset> = emptyList(),
    val issues: List<Issue> = emptyList(),
    val repairs: List<Repair> = emptyList(),
    val stats: DashboardStats = DashboardStats(),
    val isLoggedIn: Boolean = false,
    val userEmail: String = ""
)

class AppViewModel(private val repository: InventoryRepository) : ViewModel() {
    val uiState: StateFlow<InventoryUiState> = combine(
        repository.assets,
        repository.issues,
        repository.repairs
    ) { assets, issues, repairs ->
        InventoryUiState(
            assets = assets,
            issues = issues,
            repairs = repairs,
            stats = DashboardStats(
                totalAssets = assets.size,
                workingItems = assets.count { it.condition == AssetCondition.Working },
                brokenItems = assets.count { it.condition == AssetCondition.Broken },
                needsRepair = assets.count { it.condition == AssetCondition.NeedsRepair },
                missingItems = assets.count { it.condition == AssetCondition.Missing }
            )
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), InventoryUiState())

    private val session = MutableSession()

    val sessionState: StateFlow<Pair<Boolean, String>> = session.state

    fun login(email: String, password: String): String? {
        return when {
            email.isBlank() || password.isBlank() -> "Email and password are required."
            !email.contains("@") -> "Enter a valid email address."
            password.length < 6 -> "Password must be at least 6 characters."
            else -> {
                session.login(email.trim())
                null
            }
        }
    }

    fun logout() = session.logout()

    fun loginWithFirebaseUser(email: String) {
        session.login(email.ifBlank { "Google User" })
    }

    fun firebaseLogout() {
        FirebaseAuth.getInstance().signOut()
        session.logout()
    }

    fun addAsset(
        name: String,
        serial: String,
        category: String,
        section: String,
        purchaseDate: String,
        imagePath: String = "",
        condition: AssetCondition = AssetCondition.Working
    ) {
        viewModelScope.launch {
            repository.addAsset(
                Asset(
                    assetName = name.trim(),
                    serialNumber = serial.trim(),
                    category = category.trim(),
                    section = section.trim().ifBlank { "Unassigned" },
                    purchaseDate = purchaseDate.trim(),
                    condition = condition,
                    imagePath = imagePath
                )
            )
        }
    }

    fun updateConditions(assetIds: List<Long>, condition: AssetCondition) {
        viewModelScope.launch { repository.updateAssetConditions(assetIds, condition) }
    }

    fun reportIssue(assetId: Long, description: String, date: String, imagePath: String = "") {
        viewModelScope.launch {
            repository.reportIssue(
                Issue(
                    assetId = assetId,
                    issueDescription = description.trim(),
                    issueDate = date.trim(),
                    imagePath = imagePath
                )
            )
        }
    }

    fun updateRepair(repair: Repair, priority: RepairPriority? = null, status: RepairStatus? = null) {
        viewModelScope.launch {
            repository.updateRepair(
                repair.copy(
                    priority = priority ?: repair.priority,
                    repairStatus = status ?: repair.repairStatus
                )
            )
        }
    }

    class Factory(private val repository: InventoryRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T = AppViewModel(repository) as T
    }
}
