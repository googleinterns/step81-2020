import 'package:flutter/material.dart';
import 'package:macrobaseapp/model/adapters/macro_model.dart';

class MacroDetailHeader extends StatelessWidget {
  const MacroDetailHeader({
    Key key,
    @required this.macroModel,
  }) : super(key: key);

  final MacroModel macroModel;

  @override
  Widget build(BuildContext context) {
    return Container(
      child: Row(
        children: [
          Expanded(
            /*1*/
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                /*2*/
                Container(
                  padding: const EdgeInsets.only(bottom: 8),
                  child: Text(
                    macroModel.macroName,
                    style: Theme.of(context).textTheme.headline4,
                  ),
                ),
                Text(
                  macroModel.description,
                  style: Theme.of(context).textTheme.subtitle1,
                ),
              ],
            ),
          ),
          /*3*/
          Icon(
            Icons.star,
            color: Colors.red[500],
          ),
          Text(macroModel.action.type),
        ],
      ),
    );
  }
}
