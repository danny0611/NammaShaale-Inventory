package com.example.nammashaaleinventory.data

enum class AssetCondition {
    Working,
    NeedsRepair,
    Broken,
    Missing
}

enum class RepairPriority {
    Low,
    Medium,
    High
}

enum class RepairStatus {
    Open,
    InProgress,
    Resolved
}

data class Asset(
    val assetId: Long = 0,
    val assetName: String,
    val serialNumber: String,
    val category: String,
    val section: String = "Unassigned",
    val purchaseDate: String,
    val condition: AssetCondition,
    val imagePath: String = ""
)

data class Issue(
    val issueId: Long = 0,
    val assetId: Long,
    val issueDescription: String,
    val issueDate: String,
    val imagePath: String = ""
)

data class Repair(
    val repairId: Long = 0,
    val assetId: Long,
    val repairStatus: RepairStatus,
    val assignedTo: String,
    val priority: RepairPriority
)

data class DashboardStats(
    val totalAssets: Int = 0,
    val workingItems: Int = 0,
    val brokenItems: Int = 0,
    val needsRepair: Int = 0,
    val missingItems: Int = 0
)
