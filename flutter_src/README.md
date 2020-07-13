# macro-base
Platform for non-technical users to customize automation bots with G-Suite APIs

## Getting Started
[1] Enable Flutter Web Support
```
flutter channel beta
flutter upgrade
flutter config --enable-web
```
[2] Install all dependencies
```
flutter pub get
```
[3] Run the Dev Server
```
flutter run -d web-server --web-port=5000
```
[4] (Optional for Android Login) Download [googler-service.json](https://firebase.corp.google.com/project/stepladder-2020/settings/general/android:com.example.macro_base_app) and place the file in macro-base/android/app folder.

## Development
Run all the unit tests
```
flutter test test/
```
## Project Structure 
#### Lib/
* #### Presentation/
  * ##### Widgets/ - ```Contains reuseable widgets used by pages```
  * ##### Pages/ - ```Contains UI code for each page```

* #### Logic/
  * ##### Blocs/ - ```Contains the state of the current app```
  * ##### Usecases/ - ```Contains the logic ```

* #### Model/
  * ##### Entities/ - ```Contains the pure defintion of model classes```
  * ##### Adapter/ - ```Code that converts external data to entity classes```
  
#### Test/
* ##### Same folder structure as "Lib/"; Each file tests the production code. 
