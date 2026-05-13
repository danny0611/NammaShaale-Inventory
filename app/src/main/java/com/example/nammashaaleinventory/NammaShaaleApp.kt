package com.example.nammashaaleinventory

import android.app.Application
import com.example.nammashaaleinventory.data.InventoryRepository

class NammaShaaleApp : Application() {
    val repository by lazy { InventoryRepository() }
}
