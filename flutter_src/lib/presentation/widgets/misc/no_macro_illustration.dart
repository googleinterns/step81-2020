import 'package:flutter/material.dart';

class NoMacroIllustration extends StatelessWidget {
  const NoMacroIllustration({
    Key key,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: EdgeInsets.all(40),
      child: Column(
        children: <Widget>[
          Image(
            image: AssetImage("empty_macro.png"),
            height: 350,
          ),
          Text(
            "It looks empty here... Create your first macro on the left panel!",
            style: Theme.of(context).textTheme.subtitle1,
          )
        ],
      ),
    );
  }
}
