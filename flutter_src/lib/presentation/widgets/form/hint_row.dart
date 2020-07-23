import 'package:flutter/material.dart';

class HintRow extends StatelessWidget {
  const HintRow({
    Key key, this.hint,
  }) : super(key: key);

  final String hint;

  @override
  Widget build(BuildContext context) {
    return Row(
      children: [
        Icon(Icons.add_box),
        Flexible(child: Text(hint))
      ],
    );
  }
}
