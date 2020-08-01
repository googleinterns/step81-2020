// Adapted from: https://medium.com/flutter-community/flutter-implementing-google-sign-in-71888bca24edimport 'package:macrobaseapp/presentation/navigation/side_drawer_widget.dart';import 'package:macrobaseapp/presentation/navigation/setting_page.dart';
import 'package:flutter/material.dart';
import 'package:macrobaseapp/logic/api/firebase_auth.dart';
import 'package:macrobaseapp/model/entities/user.dart';
import 'package:macrobaseapp/presentation/pages/setting_page.dart';
import 'package:macrobaseapp/presentation/widgets/component/side_drawer_widget.dart';
import 'package:provider/provider.dart';

import '../pages/macro_table_page.dart';
import '../pages/new_object_page.dart';

class MainNavigator extends StatefulWidget {
  @override
  _MainNavigatorState createState() => _MainNavigatorState();
}

class _MainNavigatorState extends State<MainNavigator> {
  @override
  Widget build(BuildContext context) {
    final user = Provider.of<User>(context);

    return DefaultTabController(
      length: 3,
      child: Scaffold(
        appBar: AppBar(
          actions: <Widget>[
            IconButton(
              icon: Icon(Icons.exit_to_app),
              onPressed: () {
                context.read<FirebaseAuthService>().signOut();
              },
            )
          ],
          bottom: TabBar(
            tabs: [
              Tab(
                icon: Icon(Icons.add),
                text: 'New Chatbots',
              ),
              Tab(
                icon: Icon(Icons.view_list),
                text: 'Your Chatbots',
              ),
              Tab(
                icon: Icon(Icons.question_answer),
                text: 'Tutorial',
              ),
            ],
          ),
          title: Text("Macrobase Platform"),
        ),
        drawer: SideDrawerWidget(user: user),
        body: TabBarView(
          children: [
            NewObjectPage(),
            MacroTable(),
            TutorialPage(),
          ],
        ),
      ),
    );
  }
}
