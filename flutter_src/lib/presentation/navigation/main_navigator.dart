// Adapted from: https://medium.com/flutter-community/flutter-implementing-google-sign-in-71888bca24ed
import 'package:flutter/material.dart';
import 'package:macrobaseapp/logic/usecases/login/firebase_auth.dart';
import 'package:macrobaseapp/model/entities/user.dart';
import 'package:provider/provider.dart';

import '../pages/macro_table_page.dart';
import '../pages/new_marco_page.dart';

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
                  text: 'New Macro',
                ),
                Tab(
                  icon: Icon(Icons.view_list),
                  text: 'Macro Instances',
                ),
                Tab(
                  icon: Icon(Icons.settings),
                  text: 'Setting',
                ),
              ],
            ),
            title: Text("Macrobase Platform"),
          ),
          drawer: Drawer(
            child: ListView(
              children: <Widget>[
                Container(
                  child: DrawerHeader(
                    child: CircleAvatar(),
                  ),
                ),
                Container(
                  child: new Column(children: <Widget>[
                    Text("${user.displayName}"),
                    Text("${user.email}"),
                  ]),
                )
              ],
            ),
          ),
          body: TabBarView(
            children: [
              MyCustomForm(),
              MacroTable(),
              Center(
                child: Column(
                  children: [
                    Text(
                      "headline4",
                      style: Theme.of(context).textTheme.headline4,
                    ),
                    Text(
                      "caption",
                      style: Theme.of(context).textTheme.caption,
                    ),
                    Text(
                      "headline5",
                      style: Theme.of(context).textTheme.headline5,
                    ),
                    Text(
                      "subtitle1",
                      style: Theme.of(context).textTheme.subtitle1,
                    ),
                    Text(
                      "overline",
                      style: Theme.of(context).textTheme.overline,
                    ),
                    Text(
                      "bodyText1",
                      style: Theme.of(context).textTheme.bodyText1,
                    ),
                    Text(
                      "subtitle2",
                      style: Theme.of(context).textTheme.subtitle2,
                    ),
                    Text(
                      "bodyText2",
                      style: Theme.of(context).textTheme.bodyText2,
                    ),
                    Text(
                      "headline6",
                      style: Theme.of(context).textTheme.headline6,
                    ),
                    Text(
                      "button",
                      style: Theme.of(context).textTheme.button,
                    ),
                  ],
                ),
              ),
            ],
          ),
          floatingActionButton: FloatingActionButton(
              tooltip: 'Add', // used by assistive technologies
              child: Icon(Icons.add),
              onPressed: null),
        ));
  }
}
