import 'package:flutter/material.dart';
import 'package:macrobaseapp/model/entities/user.dart';

class SideDrawerWidget extends StatelessWidget {
  const SideDrawerWidget({
    Key key,
    @required this.user,
  }) : super(key: key);

  final User user;

  @override
  Widget build(BuildContext context) {
    return Drawer(
      child: Column(
        children: <Widget>[
          ClipOval(
            child: Image.network(user.photoUrl,
                fit: BoxFit.fill, width: 150, height: 150),
          ),
          Container(
            child: new Column(children: <Widget>[
              Text("${user.displayName}"),
              Text("${user.email}"),
            ]),
          )
        ],
      ),
    );
  }
}
