// Provider Architecture Adapted from: https://www.youtube.com/watch?v=0HLt1TYA600

import 'package:flutter/material.dart';
import 'package:macrobaseapp/model/entities/user.dart';
import 'package:macrobaseapp/presentation/pages/login_page.dart';
import 'package:macrobaseapp/presentation/navigation/main_navigator.dart';
import 'package:provider/provider.dart';

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Macrobase Platform',
      theme: ThemeData(
//        brightness: Brightness.dark,
        primaryColor: Colors.blueGrey,
//        accentColor: Colors.cyan[600],
//
//        // Define the default font family.
//        fontFamily: 'Georgia',
//
//        // Define the default TextTheme. Use this to specify the default
//        // text styling for headlines, titles, bodies of text, and more.
//        textTheme: TextTheme(
//          headline1: TextStyle(fontSize: 72.0, fontWeight: FontWeight.bold),
//          headline6: TextStyle(fontSize: 36.0, fontStyle: FontStyle.italic),
//          bodyText2: TextStyle(fontSize: 14.0, fontFamily: 'Hind'),
//        ),
      ),
      home: Consumer<User> (
        builder: (_, user, __) {
          if (user == null) {
            return LoginPage();
          } else {
            return MainNavigator();
          }
        },
      ),
    );
  }
}