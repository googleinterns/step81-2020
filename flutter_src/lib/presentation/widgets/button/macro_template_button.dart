import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

class MacroTemplateButton extends StatelessWidget {
  const MacroTemplateButton({Key key, this.templateName, this.imagePath, this.onPressed})
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
            width: 200,
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
