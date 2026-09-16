# LoanDiary 💰

Screenshot's
<img width="1440" height="3200" alt="01" src="https://github.com/user-attachments/assets/1cb97b27-a4cf-4b44-af32-95a5951b0f07" />
<img width="1440" height="3200" alt="02" src="https://github.com/user-attachments/assets/885ff9d3-4d53-42a3-b053-efb5733a907c" />
<img width="1440" height="3200" alt="03" src="https://github.com/user-attachments/assets/9ebfcf2a-c8f6-4493-8d29-4e6b35aabbed" />
<img width="1440" height="3200" alt="04" src="https://github.com/user-attachments/assets/8588a216-7b0e-4d67-884a-928cb42e5d43" />
<img width="1440" height="3200" alt="05" src="https://github.com/user-attachments/assets/0bcb8cad-5afb-45a6-b452-31f058fc9b96" />
<img width="1440" height="3945" alt="06" src="https://github.com/user-attachments/assets/7a168cea-4c56-47b2-8cd8-7acbc3ffaad5" />
<img width="1440" height="3959" alt="07" src="https://github.com/user-attachments/assets/ec9bcb1d-c148-4b4e-82bd-850fff31d8d7" />



LoanDiary is a modern, secure, and lightweight Android application built to help you track your personal finances, specifically focusing on money lent to and borrowed from others. 

## 🚀 Key Features

- **Loan Tracking**: Easily log money you've lent to friends or borrowed from others.
- **Repayment Management**: Track partial repayments and automatically update loan status.
- **Biometric Security**: Protect your financial data with Fingerprint or Face ID authentication.
- **Customizable Appearance**: Choose from multiple built-in themes (Professional, Midnight, Forest, etc.) to suit your style.
- **Persistent Settings**: Your theme preferences, default currency, and security settings are saved automatically using Jetpack DataStore.
- **Data Export**: Export your loan records as a CSV file for backup or reporting.
- **Contact Integration**: Quickly pick names from your phone's contact list.

## 🛠️ Technical Stack

- **UI Framework**: [Jetpack Compose](https://developer.android.com/jetpack/compose) for a modern, declarative UI.
- **Architecture**: MVVM (Model-View-ViewModel) for clean separation of concerns.
- **Dependency Injection**: [Hilt](https://developer.android.com/training/dependency-injection/hilt-android) for robust and scalable DI.
- **Database**: [Room](https://developer.android.com/training/data-storage/room) for reliable local SQLite storage of loans.
- **Key-Value Storage**: [Jetpack DataStore](https://developer.android.com/topic/libraries/architecture/datastore) for persistent user preferences.
- **Security**: [Biometric API](https://developer.android.com/training/sign-in/biometric-auth) for secure app access.
- **Concurrency**: Kotlin Coroutines and Flow for reactive data updates.

## 📂 Project Structure & Functions

### Data Layer (`com.ajshahariar.loandiary.data`)
- **`Loan`**: The core data model representing a financial record (Amount, Type, Currency, Note, etc.).
- **`LoanDao`**: Defines the database operations (Insert, Update, Delete, Query).
- **`SettingsRepository`**: Manages app-wide preferences like theme index and biometric status using DataStore.

### UI & ViewModel (`com.ajshahariar.loandiary.ui`)
- **`LoanViewModel`**: The bridge between the UI and Data layer. It handles business logic, loan updates, and settings persistence.
- **`HomeScreen`**: Provides a summary of all active loans with filtering and status indicators.
- **`AddLoanScreen`**: Form to create new records with contact picker and date selection.
- **`LoanDetailScreen`**: View details of a specific loan and manage repayments.
- **`SettingsScreen`**: Configuration hub for themes, currency, and security.

### Theme Engine (`com.ajshahariar.loandiary.ui.theme`)
- **`CustomThemePalette`**: A custom system that allows the app to switch between entirely different color schemes dynamically without restarting the Activity.

## 🛠️ How to Build

1. Clone the repository.
2. Open the project in **Android Studio (Ladybug or newer)**.
3. Sync the Gradle files.
4. Run the app on an emulator or physical device.

Alternatively, build the debug APK via terminal:
```bash
./gradlew :app:assembleDebug
```

## 🤝 Contributing
Feel free to fork this project and submit pull requests. For major changes, please open an issue first to discuss what you would like to change.

## 📝 License

Copyright (c) 2026 Golam Nawous Shahariar (AJ). All Rights Reserved.

This software and its source code are proprietary and confidential.
Unauthorized copying, distribution, modification, or use of this
software, via any medium, is strictly prohibited without express
written permission from the copyright holder.

No license, express or implied, to any intellectual property rights
is granted by this document.
