# macro-base
Platform for non-technical users to customize automation bots with G-Suite APIs

## Getting Started
[1] Install Flutter and Create a Firebase Project with Web Support\
[2] Set up Firebase User Authentication and Cloud Firestore\
[3] Enable Flutter Web Support
```
flutter channel beta
flutter upgrade
flutter config --enable-web
```
[4] Install all Dependencies
```
flutter pub get
```
[5] Run the Local Development Server
```
flutter run -d web-server --web-port=5000
```
[6] (Optional for Android Login) Download [googler-service.json](https://firebase.corp.google.com/project/stepladder-2020/settings/general/android:com.example.macro_base_app) and place the file in macro-base/android/app folder.

## Development
Run all the unit tests
```
flutter test test/
```
## Deployment
[1] Set up Firebase CLI [Firebase Hosting guide](https://firebase.google.com/docs/hosting)
*Make sure you have Firebase project set up already*
```
curl -sL https://firebase.tools | bash
firebase login
```
[2] Build Flutter Web App
```
flutter web build
```
[3] Initalize Firebase Deployment
```
firebase --init
# Select Hosting: Configure and deploy Firebase Hosting sites feature
# Set build/web as Public directory
```
[4] Deploy using Firebase
```
firebase deploy --only hosting
```
[5] Whitelist Hosting URL in Web Client GCP OAuth 2.0 Client IDs\
[6] You're done!

## Project Structure 
App\
├── Lib/\
│   ├── Presentation/\
│   │   ├── Widgets: Contains reuseable widgets used by pages\
│   │   └── Pages: Contains UI code for each page\
│   ├── Logic/\
│   │   ├── Blocs: Contains the state of the current app\
│   │   └── Usecases: Contains the logic\
│   └── Model/\
│       ├── Entities: Contains the pure defintion of model classes\
│       └── Adapter: Code that converts external data to entity classes\
└── Test: Each file tests the production code.\
