import 'package:flutter/material.dart';

class NormalButton extends StatelessWidget {
  const NormalButton({
    Key key,
    @required this.context, this.text, this.onPress,
  }) : super(key: key);

  final BuildContext context;
  final String text;
  final Function onPress;

  @override
  Widget build(BuildContext context) {
    return RaisedButton(
        color: Theme.of(context).colorScheme.secondary,
        onPressed: onPress,
        child: Text(
          text,
          style: Theme.of(context).textTheme.button,
        ));
  }
}
