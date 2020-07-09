import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

class macro_template_button extends StatelessWidget {
  const macro_template_button({Key key, this.templateName}) : super(key: key);

  final String templateName;

  @override
  Widget build(BuildContext context) {
    return Row(
      children: [
        CupertinoButton(
          child: Container(
            height: 200,
            width: 200,
            decoration: BoxDecoration(
              image: DecorationImage(
                image: AssetImage("assets/mindful.jpg"),
                fit: BoxFit.cover,
              ),
              borderRadius: BorderRadius.circular(12),
            ),
            child: Container(
              margin: EdgeInsets.fromLTRB(15, 15, 0, 0),
              child: Text(
                "Mindfulness",
                style: TextStyle(
                    color: Colors.white,
                    fontSize: 20,
                    fontWeight: FontWeight.w600),
              ),
            ),
          ),
        ),
        SizedBox(width: 50),
      ],
    );
  }
}
