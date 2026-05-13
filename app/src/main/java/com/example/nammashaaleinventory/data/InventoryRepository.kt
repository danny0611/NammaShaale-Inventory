package com.example.nammashaaleinventory.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.tasks.await

class InventoryRepository(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance(),
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) {
    private val assetsCollection = firestore.collection("assets")
    private val issuesCollection = firestore.collection("issues")
    private val repairsCollection = firestore.collection("repairs")

    private val _assets = MutableStateFlow<List<Asset>>(emptyList())
    private val _issues = MutableStateFlow<List<Issue>>(emptyList())
    private val _repairs = MutableStateFlow<List<Repair>>(emptyList())

    val assets: Flow<List<Asset>> = _assets
    val issues: Flow<List<Issue>> = _issues
    val repairs: Flow<List<Repair>> = _repairs

    init {
        assetsCollection.orderBy("assetName").addSnapshotListener { snapshot, error ->
            if (error != null) {
                return@addSnapshotListener
            }

            val assets = snapshot?.documents.orEmpty().mapNotNull { document ->
                document.data?.toAsset(document.id)
            }
            _assets.value = assets
        }

        issuesCollection.orderBy("issueDate", Query.Direction.DESCENDING).addSnapshotListener { snapshot, error ->
            if (error != null) {
                return@addSnapshotListener
            }

            val issues = snapshot?.documents.orEmpty().mapNotNull { document ->
                document.data?.toIssue(document.id)
            }
            _issues.value = issues
        }

        repairsCollection.orderBy("repairId", Query.Direction.DESCENDING).addSnapshotListener { snapshot, error ->
            if (error != null) {
                return@addSnapshotListener
            }

            val repairs = snapshot?.documents.orEmpty().mapNotNull { document ->
                document.data?.toRepair(document.id)
            }
            _repairs.value = repairs
        }
    }

    suspend fun addAsset(asset: Asset): Long {
        val cloudAsset = asset.copy(assetId = asset.assetId.nonZeroId())
        _assets.update { current ->
            (current.filterNot { it.assetId == cloudAsset.assetId } + cloudAsset).sortedBy { it.assetName }
        }
        runCatching {
            ensureFirebaseSession()
            assetsCollection.document(cloudAsset.assetId.toString()).set(cloudAsset.toFirestoreMap()).await()
        }
        return cloudAsset.assetId
    }

    suspend fun updateAsset(asset: Asset) {
        _assets.update { current ->
            current.map { if (it.assetId == asset.assetId) asset else it }.sortedBy { it.assetName }
        }
        runCatching {
            ensureFirebaseSession()
            assetsCollection.document(asset.assetId.toString()).set(asset.toFirestoreMap()).await()
        }
    }

    suspend fun updateAssetConditions(assetIds: List<Long>, condition: AssetCondition) {
        _assets.update { current ->
            current.map { asset ->
                if (asset.assetId in assetIds) asset.copy(condition = condition) else asset
            }
        }
        ensureFirebaseSession()
        assetIds.forEach { assetId ->
            runCatching {
                assetsCollection.document(assetId.toString()).update("condition", condition.name).await()
            }
        }
    }

    suspend fun reportIssue(issue: Issue): Long {
        val cloudIssue = issue.copy(issueId = issue.issueId.nonZeroId())
        _issues.update { current ->
            (current.filterNot { it.issueId == cloudIssue.issueId } + cloudIssue).sortedByDescending { it.issueDate }
        }

        _assets.update { current ->
            current.map { asset ->
                if (asset.assetId == issue.assetId) asset.copy(condition = AssetCondition.NeedsRepair) else asset
            }
        }

        val repair = Repair(
            repairId = 0L.nonZeroId(),
            assetId = issue.assetId,
            repairStatus = RepairStatus.Open,
            assignedTo = "School Admin",
            priority = RepairPriority.Medium
        )
        _repairs.update { current ->
            (current.filterNot { it.repairId == repair.repairId } + repair).sortedByDescending { it.repairId }
        }

        runCatching {
            ensureFirebaseSession()
            issuesCollection.document(cloudIssue.issueId.toString()).set(cloudIssue.toFirestoreMap()).await()
            assetsCollection.document(issue.assetId.toString()).update("condition", AssetCondition.NeedsRepair.name).await()
            repairsCollection.document(repair.repairId.toString()).set(repair.toFirestoreMap()).await()
        }
        return cloudIssue.issueId
    }

    suspend fun updateRepair(repair: Repair) {
        _repairs.update { current ->
            current.map { if (it.repairId == repair.repairId) repair else it }.sortedByDescending { it.repairId }
        }
        runCatching {
            ensureFirebaseSession()
            repairsCollection.document(repair.repairId.toString()).set(repair.toFirestoreMap()).await()
        }
    }

    private suspend fun ensureFirebaseSession() {
        if (auth.currentUser == null) {
            runCatching { auth.signInAnonymously().await() }
        }
    }
}

private fun Asset.toFirestoreMap(): Map<String, Any> = mapOf(
    "assetId" to assetId,
    "assetName" to assetName,
    "serialNumber" to serialNumber,
    "category" to category,
    "section" to section,
    "purchaseDate" to purchaseDate,
    "condition" to condition.name,
    "imagePath" to imagePath
)

private fun Issue.toFirestoreMap(): Map<String, Any> = mapOf(
    "issueId" to issueId,
    "assetId" to assetId,
    "issueDescription" to issueDescription,
    "issueDate" to issueDate,
    "imagePath" to imagePath
)

private fun Repair.toFirestoreMap(): Map<String, Any> = mapOf(
    "repairId" to repairId,
    "assetId" to assetId,
    "repairStatus" to repairStatus.name,
    "assignedTo" to assignedTo,
    "priority" to priority.name
)

private fun Map<String, Any>.toAsset(documentId: String): Asset? {
    val id = longValue("assetId") ?: documentId.toLongOrNull() ?: return null
    return Asset(
        assetId = id,
        assetName = stringValue("assetName"),
        serialNumber = stringValue("serialNumber"),
        category = stringValue("category"),
        section = stringValue("section").ifBlank { "Unassigned" },
        purchaseDate = stringValue("purchaseDate"),
        condition = enumValue("condition", AssetCondition.Working),
        imagePath = stringValue("imagePath")
    )
}

private fun Map<String, Any>.toIssue(documentId: String): Issue? {
    val id = longValue("issueId") ?: documentId.toLongOrNull() ?: return null
    val assetId = longValue("assetId") ?: return null
    return Issue(
        issueId = id,
        assetId = assetId,
        issueDescription = stringValue("issueDescription"),
        issueDate = stringValue("issueDate"),
        imagePath = stringValue("imagePath")
    )
}

private fun Map<String, Any>.toRepair(documentId: String): Repair? {
    val id = longValue("repairId") ?: documentId.toLongOrNull() ?: return null
    val assetId = longValue("assetId") ?: return null
    return Repair(
        repairId = id,
        assetId = assetId,
        repairStatus = enumValue("repairStatus", RepairStatus.Open),
        assignedTo = stringValue("assignedTo").ifBlank { "School Admin" },
        priority = enumValue("priority", RepairPriority.Medium)
    )
}

private fun Map<String, Any>.stringValue(key: String): String = this[key] as? String ?: ""

private fun Map<String, Any>.longValue(key: String): Long? = when (val value = this[key]) {
    is Long -> value
    is Int -> value.toLong()
    is Double -> value.toLong()
    is Number -> value.toLong()
    is String -> value.toLongOrNull()
    else -> null
}

private inline fun <reified T : Enum<T>> Map<String, Any>.enumValue(key: String, fallback: T): T {
    val value = this[key] as? String ?: return fallback
    return enumValues<T>().firstOrNull { it.name == value } ?: fallback
}

private fun Long.nonZeroId(): Long = takeIf { it != 0L } ?: System.currentTimeMillis()
