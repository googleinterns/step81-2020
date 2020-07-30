import 'package:flutter/material.dart';

class TutorialPage extends StatelessWidget {
  const TutorialPage({
    Key key,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Center(
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
    );
  }
}
