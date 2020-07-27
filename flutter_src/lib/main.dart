import 'package:flutter/material.dart';
import 'package:macrobaseapp/logic/state/macro_state/macro_notifier.dart';
import 'package:macrobaseapp/logic/usecases/login/firebase_auth.dart';
import 'package:macrobaseapp/presentation/app.dart';
import 'package:provider/provider.dart';

void main() => runApp(
  /// Inject the [FirebaseAuthService]
  /// and provide a stream of [User]
  ///
  /// This needs to be above [MaterialApp]
  /// At the top of the widget tree, to
  /// accomodate for navigations in the app
  MultiProvider(
    providers: [
      ChangeNotifierProvider(
        create: (context) => MacroNotifier(),
      ),
      Provider(
        create: (_) => FirebaseAuthService(),
      ),
      StreamProvider(
        create: (context) =>
          context.read<FirebaseAuthService>().onAuthStateChanged,
      ),
    ],
    child: MyApp(),
  ),
);
