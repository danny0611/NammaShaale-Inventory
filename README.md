1. Project Overview
Project Title
Android App Development using GenAI – Namma-Shaale Inventory
Project Description
Namma-Shaale Inventory is a smart Android-based inventory management application designed for government schools to digitally track and manage school assets such as sports kits, lab equipment, tablets, and classroom resources.
The application acts as a “Digital Asset Auditor” where teachers and administrators can:


Register assets


Update asset condition


Report damaged or missing items


Track repair requests


Generate summary reports


The app improves:


Resource management


Accountability


Educational quality


Asset maintenance efficiency



2. App Architecture & User Flow
App Architecture
The application follows a modern Android architecture:
Presentation Layer (UI)↓ViewModel Layer↓Repository Layer↓Room Database / API

User Flow
Teacher Flow
Login→ Dashboard→ Add Asset→ Update Condition→ Report Issue→ Generate Reports

Admin Flow
Login→ Dashboard Analytics→ Monitor Assets→ Approve Repairs→ Export Reports

3. Complete Screen-by-Screen Specifications
Screen 1 – Splash Screen
Features


App logo


Loading animation


App title


Purpose
Initial branding and app loading.

Screen 2 – Login Screen
Components


Email field


Password field


Login button


Forgot password option


Validation


Empty field validation


Invalid credentials message



Screen 3 – Dashboard Screen
Features


Total Assets


Working Items


Broken Items


Needs Repair Count


Quick action buttons


Analytics chart


UI Elements


Cards


Pie chart / bar chart


Bottom navigation



Screen 4 – Asset Registration Screen
Input Fields


Asset Name


Serial Number


Category


Purchase Date


Asset Photo Upload


Actions


Save asset


Cancel



Screen 5 – Asset List Screen
Features


Search bar


Filter by category


Filter by condition


RecyclerView/Grid layout


Actions


View details


Edit asset



Screen 6 – Condition Update Screen
Features


Monthly health check


Status selection:


Working


Needs Repair


Broken




Actions


Bulk update


Save updates



Screen 7 – Issue Reporting Screen
Features


Select asset


Enter issue description


Upload image


Select issue date


Example
“Football lost during match”

Screen 8 – Repair Request Screen
Features


View repair list


Assign repair priority


Mark issue resolved



Screen 9 – Reports Screen
Features


Monthly report generation


Export PDF option


Asset statistics



Screen 10 – Profile Screen
Features


User information


Logout option


Settings



4. Backend & Database Structure
Local Database
Room Database will be used for offline storage.

Tables Structure
Asset Table
FieldTypeassetIdIntegerassetNameStringserialNumberStringcategoryStringconditionStringimagePathString

Issue Table
FieldTypeissueIdIntegerassetIdIntegerissueDescriptionStringissueDateString

Repair Table
FieldTyperepairIdIntegerassetIdIntegerrepairStatusStringassignedToString

5. Authentication System
Authentication Type


Firebase Authentication


Login Methods


Email & Password Login


Security Features


Secure authentication


Session management


User role validation



6. Feature Requirements
Functional Requirements


User login/logout


Asset registration


Asset health tracking


Issue reporting


Repair management


Dashboard analytics


Report generation



Non-Functional Requirements


Fast performance


Simple UI


Offline capability


Data security


Scalability


Responsive design



7. Technology Stack
ComponentTechnologyFrontendAndroid XML / Jetpack ComposeLanguageKotlinDatabaseRoom DBAuthenticationFirebase AuthArchitectureMVVMIDEAndroid StudioVersion ControlGit & GitHub

8. API & Third-Party Integrations
Integrations Used


Firebase Authentication


CameraX API


Room Database


PDF Generator Library



Optional Future APIs


Cloud Storage API


AI Prediction API for maintenance alerts



9. Development Timeline
PhaseDurationRequirement Analysis2 DaysUI/UX Design3 DaysFrontend Development5 DaysBackend Integration4 DaysDatabase Implementation2 DaysTesting3 DaysFinal Deployment1 Day

10. Testing Checklist
Functional Testing


Login testing


Add asset testing


Update condition testing


Report generation testing



UI Testing


Responsive layouts


Navigation flow


Button functionality



Performance Testing


App loading speed


Database performance



11. Deployment Instructions
Steps to Deploy


Open project in Android Studio


Sync Gradle files


Connect Android device/emulator


Build APK


Run application


Test all modules


Generate signed APK for final deployment



Expected Outcome
The application will provide:


Efficient school inventory management


Digital tracking of assets


Faster maintenance reporting


Better accountability in government schools


Easy asset auditing process for teachers and administrators

