📱 Overview

Namma Shaale Inventory Management System is an Android application developed to digitize and simplify school inventory management processes. The application enables administrators to track assets such as sports kits, laboratory equipment, tablets, classroom accessories, and other institutional resources through a centralized and user-friendly platform.

The system provides functionalities such as asset tracking, condition monitoring, issue reporting, repair management, and dashboard analytics using modern Android development technologies and Firebase cloud integration.

✨ Features
🔐 Authentication System
Secure login using Firebase Authentication
User session management
Protected access to inventory data
📦 Asset Management
Add and manage inventory assets
Store asset details and categories
Maintain serial numbers and purchase information
Real-time asset updates
🛠️ Condition Monitoring

Track asset health using conditions such as:

✅ Working
⚠️ Needs Repair
❌ Broken
🚫 Missing
📝 Issue Reporting
Report damaged or missing assets
Store issue descriptions and timestamps
Maintain centralized issue records
🔧 Repair Management
Track repair requests and repair status
Priority-based repair handling
Repair workflow monitoring
📊 Dashboard Analytics

View real-time inventory insights:

Total Assets
Working Assets
Broken Assets
Missing Assets
Repair Required Assets
☁️ Cloud Integration
Firebase Firestore integration
Real-time data synchronization
Secure cloud-based storage
🏗️ Tech Stack
Technology	Purpose
Kotlin	Android Application Development
Jetpack Compose	Modern UI Development
Firebase Firestore	Cloud Database
Firebase Authentication	User Authentication
Android Studio	Development Environment
Material Design 3	UI Components & Styling
Kotlin Coroutines	Asynchronous Operations
MVVM Architecture	Scalable Application Structure
📂 Project Structure
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
⚙️ System Architecture
User Interface (Jetpack Compose)
            ↓
        ViewModel
            ↓
       Repository
            ↓
 Firebase Firestore Database
🚀 Getting Started
📋 Prerequisites

Before running the project, ensure you have:

Android Studio installed
Firebase project configured
Android SDK installed
Kotlin support enabled
🔥 Firebase Setup
Create a Firebase project from the Firebase Console
Enable:
Firebase Authentication
Cloud Firestore
Download the google-services.json file
Place it inside:
app/google-services.json
▶️ Installation Steps
1️⃣ Clone the Repository
git clone https://github.com/your-username/namma-shaale-inventory.git
2️⃣ Open in Android Studio
File → Open → Select Project Folder
3️⃣ Sync Gradle

Allow Android Studio to download dependencies.

4️⃣ Run the Application

Connect an Android device or emulator and click:

Run ▶️
📸 Application Modules
Module	Description
Authentication	Secure user login
Asset Management	Add/manage inventory assets
Condition Tracking	Monitor asset health
Issue Reporting	Report inventory problems
Repair Management	Track repairs and priorities
Dashboard Analytics	Visual inventory insights
🧠 Learning Outcomes

Through this project, practical experience was gained in:

Android Application Development
Kotlin Programming
Jetpack Compose UI Design
Firebase Firestore Integration
Firebase Authentication
MVVM Architecture
Real-Time Database Synchronization
State Management
Cloud-Based Mobile Applications
🛡️ Advantages
Reduces manual paperwork
Centralized inventory management
Real-time cloud synchronization
Faster issue handling
Improved transparency
Easy monitoring through dashboards
Scalable and maintainable architecture
🔮 Future Enhancements
QR/Barcode Scanning
AI-based Predictive Maintenance
Push Notifications
Offline Synchronization
Multi-role Authentication
Web Dashboard Integration
Export Reports in PDF/Excel
Advanced Analytics
🧪 Testing

The application was tested for:

Authentication Flow
Firebase Connectivity
Asset Management Operations
Dashboard Calculations
Repair Workflow
UI Responsiveness
Real-Time Synchronization
👨‍💻 Developed By

Dhanush M

Android Developer | AIML Student | Firebase Enthusiast

📄 License

This project is developed for educational and learning purposes.

⭐ Support

If you like this project:

⭐ Star the repository
🍴 Fork the project
📢 Share your feedback
