# 📦 Namma Shaale Inventory Management System

> A modern Android-based inventory management application designed to help educational institutions efficiently manage, monitor, and maintain school assets using real-time cloud technologies.

---

## 📱 Overview

**Namma Shaale Inventory Management System** is an Android application developed to digitize and simplify school inventory management processes. The application enables administrators to track assets such as sports kits, laboratory equipment, tablets, classroom accessories, and other institutional resources through a centralized and user-friendly platform.

The system provides functionalities such as asset tracking, condition monitoring, issue reporting, repair management, and dashboard analytics using modern Android development technologies and Firebase cloud integration.

---

## ✨ Features

### 🔐 Authentication System
- Secure login using Firebase Authentication
- User session management
- Protected access to inventory data

### 📦 Asset Management
- Add and manage inventory assets
- Store asset details and categories
- Maintain serial numbers and purchase information
- Real-time asset updates

### 🛠️ Condition Monitoring
Track asset health using conditions such as:
- ✅ Working
- ⚠️ Needs Repair
- ❌ Broken
- 🚫 Missing

### 📝 Issue Reporting
- Report damaged or missing assets
- Store issue descriptions and timestamps
- Maintain centralized issue records

### 🔧 Repair Management
- Track repair requests and repair status
- Priority-based repair handling
- Repair workflow monitoring

### 📊 Dashboard Analytics
View real-time inventory insights:
- Total Assets
- Working Assets
- Broken Assets
- Missing Assets
- Repair Required Assets

### ☁️ Cloud Integration
- Firebase Firestore integration
- Real-time data synchronization
- Secure cloud-based storage

---

# 🏗️ Tech Stack

| Technology | Purpose |
|---|---|
| Kotlin | Android Application Development |
| Jetpack Compose | Modern UI Development |
| Firebase Firestore | Cloud Database |
| Firebase Authentication | User Authentication |
| Android Studio | Development Environment |
| Material Design 3 | UI Components & Styling |
| Kotlin Coroutines | Asynchronous Operations |
| MVVM Architecture | Scalable Application Structure |

---

# 📂 Project Structure

```bash
NammaShaaleInventory/
│
├── app/
│   ├── src/main/java/com/example/nammashaaleinventory/
│   │
│   ├── data/
│   │   ├── Models.kt
│   │   ├── InventoryRepository.kt
│   │
│   ├── ui/
│   │   ├── AppViewModel.kt
│   │   ├── MutableSession.kt
│   │
│   ├── MainActivity.kt
│   ├── NammaShaaleApp.kt
│
├── gradle/
├── build.gradle
├── settings.gradle
