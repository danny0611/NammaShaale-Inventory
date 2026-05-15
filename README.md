<div align="center">

<img src="https://cdn-icons-png.flaticon.com/512/3135/3135755.png" width="120"/>

# 🏫 Namma Shaale Inventory Management System

### 📦 Smart School Inventory Monitoring & Maintenance Platform

<img src="https://img.shields.io/badge/Platform-Android-green?style=for-the-badge"/>
<img src="https://img.shields.io/badge/Kotlin-Android%20Development-blue?style=for-the-badge"/>
<img src="https://img.shields.io/badge/Firebase-Cloud%20Backend-orange?style=for-the-badge"/>
<img src="https://img.shields.io/badge/Jetpack%20Compose-Modern%20UI-purple?style=for-the-badge"/>
<img src="https://img.shields.io/badge/Architecture-MVVM-red?style=for-the-badge"/>

---

### 🚀 Digitizing School Inventory Management with Real-Time Cloud Technology

</div>

---

# 🌟 Project Overview

The **Namma Shaale Inventory Management System** is a modern Android-based application developed to simplify and digitize inventory management processes in educational institutions.

The application helps schools efficiently manage and monitor:

- 🏀 Sports equipment
- 💻 Tablets & electronic devices
- 🔬 Laboratory equipment
- 🪑 Classroom resources
- 📚 Educational assets

The system provides a centralized cloud-based platform for:

✅ Asset Management  
✅ Classroom-wise Inventory Tracking  
✅ Condition Monitoring  
✅ Issue Reporting  
✅ Repair Management  
✅ Dashboard Analytics  
✅ Real-Time Synchronization  

The project was developed using **Kotlin**, **Jetpack Compose**, **Firebase Firestore**, and **Firebase Authentication** following modern Android development practices and MVVM architecture.

---

# ✨ Key Features

---

## 🔐 Secure Authentication System

<div align="center">
<img src="https://cdn-icons-png.flaticon.com/512/3064/3064155.png" width="80"/>
</div>

- Firebase Authentication integration
- Secure login and session management
- Protected inventory access
- User authentication workflows

---

## 📦 Asset Management Module

<div align="center">
<img src="https://cdn-icons-png.flaticon.com/512/679/679720.png" width="80"/>
</div>

### Features:
- Add and manage assets
- Store inventory details
- Manage serial numbers
- Categorize inventory records
- Real-time asset updates
- Cloud-based synchronization

---

## 🏫 Classroom-wise Inventory Monitoring

<div align="center">
<img src="https://cdn-icons-png.flaticon.com/512/1048/1048953.png" width="80"/>
</div>

### Smart Classroom Tracking

- View inventory assigned to each classroom
- Identify broken or repair-required assets class-wise
- Display complete classroom asset lists
- Improve maintenance transparency

---

## 🛠️ Condition Monitoring System

<div align="center">
<img src="https://cdn-icons-png.flaticon.com/512/2910/2910791.png" width="80"/>
</div>

### Asset Conditions

| Status | Description |
|---|---|
| ✅ Working | Asset functioning properly |
| ⚠️ Needs Repair | Maintenance required |
| ❌ Broken | Asset damaged |
| 🚫 Missing | Asset unavailable |

---

## 📝 Issue Reporting Module

<div align="center">
<img src="https://cdn-icons-png.flaticon.com/512/1828/1828919.png" width="80"/>
</div>

- Report damaged assets
- Track missing inventory
- Maintain issue records
- Improve maintenance workflows

---

## 🔧 Repair Management System

<div align="center">
<img src="https://cdn-icons-png.flaticon.com/512/942/942748.png" width="80"/>
</div>

### Repair Workflow Features

- Repair status tracking
- Priority-based maintenance
- Repair progress monitoring
- Organized maintenance handling

### Repair Status
- Open
- In Progress
- Resolved

---

## 📊 Dashboard Analytics

<div align="center">
<img src="https://cdn-icons-png.flaticon.com/512/1828/1828884.png" width="80"/>
</div>

### Dashboard Displays

📦 Total Assets  
✅ Working Assets  
⚠️ Repair Required Assets  
❌ Broken Assets  
🚫 Missing Assets  
🏫 Classroom-wise Inventory Status  

---

# 📸 Application Screenshots

> Add your application screenshots inside the `screenshots/` folder.

| Login Screen | Dashboard |
|---|---|
| ![](screenshots/login.png) | ![](screenshots/dashboard.png) |

| Classroom Inventory | Repair Management |
|---|---|
| ![](screenshots/classroom.png) | ![](screenshots/repair.png) |

| Asset Management | Analytics |
|---|---|
| ![](screenshots/assets.png) | ![](screenshots/analytics.png) |

---

# 🏗️ System Architecture

```text
User Interface (Jetpack Compose)
                ↓
            ViewModel
                ↓
           Repository
                ↓
 Firebase Firestore Database
```

The application follows the **MVVM Architecture Pattern** for scalability, maintainability, and clean separation of concerns.

---

# 🔄 Application Workflow

```text
User Login
     ↓
Dashboard Access
     ↓
Add / Manage Assets
     ↓
Track Asset Conditions
     ↓
Report Issues
     ↓
Repair Management
     ↓
Real-Time Dashboard Monitoring
```

---

# ☁️ Firebase Integration

<div align="center">

<img src="https://firebase.google.com/static/images/brand-guidelines/logo-logomark.png" width="90"/>

</div>

### Firebase Services Used

🔥 Firebase Authentication  
🔥 Firebase Firestore  
🔥 Real-Time Cloud Synchronization  

---

# 🧠 Technologies Used

| Technology | Purpose |
|---|---|
| Kotlin | Android Application Development |
| Jetpack Compose | Modern UI Development |
| Firebase Firestore | Cloud Database |
| Firebase Authentication | Secure Login System |
| Android Studio | Development Environment |
| Material Design 3 | UI Components |
| Kotlin Coroutines | Background Operations |
| MVVM Architecture | Scalable App Structure |
| Navigation Compose | Screen Navigation |
| Git & GitHub | Version Control |

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
├── screenshots/
├── build.gradle
├── settings.gradle
└── README.md
```

---

# ⚙️ Installation Guide

## 📋 Prerequisites

Before running the project, install:

- Android Studio
- Android SDK
- Kotlin Support
- Firebase Account

---

# 🔥 Firebase Setup

1. Create a Firebase project
2. Enable:
   - Firebase Authentication
   - Cloud Firestore

3. Download:

```bash
google-services.json
```

4. Place it inside:

```bash
app/google-services.json
```

---

# ▶️ Running the Project

## Clone Repository

```bash
git clone https://github.com/your-username/namma-shaale-inventory.git
```

## Open in Android Studio

```bash
File → Open → Select Project Folder
```

## Sync Gradle

Allow dependencies to download automatically.

## Run Application

Connect Android device/emulator and click:

```bash
Run ▶️
```

---

# 🧪 Testing

The application was tested for:

✅ Firebase Authentication  
✅ Dashboard Calculations  
✅ Asset Management Operations  
✅ Classroom-wise Inventory Tracking  
✅ Repair Workflow Validation  
✅ Real-Time Synchronization  
✅ Navigation Flow  
✅ UI Responsiveness  

---

# 📈 Learning Outcomes

This project helped in gaining practical experience in:

- Android Application Development
- Kotlin Programming
- Firebase Integration
- Cloud Database Management
- MVVM Architecture
- Real-Time Synchronization
- Jetpack Compose UI Design
- Application Testing & Debugging
- Inventory Management Workflows

---

# 🛡️ Advantages of the System

✅ Eliminates manual paperwork  
✅ Improves inventory transparency  
✅ Real-time cloud synchronization  
✅ Faster maintenance tracking  
✅ Classroom-wise monitoring  
✅ Organized repair management  
✅ Scalable architecture  
✅ User-friendly Android interface  

---

# 🔮 Future Enhancements

🚀 QR / Barcode Scanning  
🚀 AI-based Predictive Maintenance  
🚀 Push Notifications  
🚀 Offline Synchronization  
🚀 Multi-role Authentication  
🚀 Web Dashboard Support  
🚀 PDF / Excel Report Export  
🚀 Advanced Analytics  

---

# 👨‍💻 Developer

<div align="center">

<img src="https://cdn-icons-png.flaticon.com/512/3135/3135715.png" width="120"/>

## Dhanush M

### Android Developer | AIML Student | Firebase Enthusiast

</div>

---

# ⭐ Support the Project

If you like this project:

⭐ Star the Repository  
🍴 Fork the Project  
📢 Share Feedback  
🤝 Connect for Collaboration  

---

# 📜 License

This project was developed for educational and learning purposes.

---

<div align="center">

# 💙 Thank You for Visiting

### “Technology becomes meaningful when it solves real-world problems.”

<img src="https://cdn-icons-png.flaticon.com/512/833/833472.png" width="60"/>

</div>
