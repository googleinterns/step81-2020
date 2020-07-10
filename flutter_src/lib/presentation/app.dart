// Provider Architecture Adapted from: https://www.youtube.com/watch?v=0HLt1TYA600

import 'package:flutter/material.dart';
import 'package:macrobaseapp/model/entities/user.dart';
import 'package:macrobaseapp/presentation/pages/login_page.dart';
import 'package:macrobaseapp/presentation/navigation/main_navigator.dart';
import 'package:macrobaseapp/presentation/theme.dart';
import 'package:provider/provider.dart';

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Macrobase Platform',
      theme: GalleryThemeData.darkThemeData,
      home: Consumer<User>(
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
