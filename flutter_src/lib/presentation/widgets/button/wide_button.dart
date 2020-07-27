import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

class WideButton extends StatelessWidget {
  const WideButton(
      {Key key,
        @required this.templateName,
        @required this.imagePath,
        @required this.onPressed})
      : super(key: key);

  final String templateName;
  final String imagePath;
  final Function onPressed;

  @override
  Widget build(BuildContext context) {
    return Row(
      children: [
        CupertinoButton(
          onPressed: onPressed,
          child: Container(
            height: 200,
            width: 400,
            decoration: BoxDecoration(
              image: DecorationImage(
                image: AssetImage(imagePath),
                fit: BoxFit.cover,
              ),
              borderRadius: BorderRadius.circular(12),
            ),
            child: Container(
              margin: EdgeInsets.only(left: 15, top: 15),
              child: Text(
                templateName,
                style: Theme.of(context).textTheme.subtitle1.copyWith(
                    backgroundColor: Theme.of(context).colorScheme.background),
              ),
            ),
          ),
        ),
        SizedBox(width: 30),
      ],
    );
  }
}
