import 'package:flutter/material.dart';
import 'package:flutter_form_bloc/flutter_form_bloc.dart';

class MyStatefulWidget extends StatefulWidget {
  MyStatefulWidget({Key key, @required this.options, @required this.bloc})
      : super(key: key);

  final List<String> options;
  final TextFieldBloc bloc;

  @override
  _MyStatefulWidgetState createState() =>
      _MyStatefulWidgetState(options, bloc);
}

class _MyStatefulWidgetState extends State<MyStatefulWidget> {
  List<String> options;
  TextFieldBloc bloc;
  String dropdownValue;

  _MyStatefulWidgetState(this.options, this.bloc) {
    dropdownValue = options[0];
  }

  @override
  Widget build(BuildContext context) {
    return Container(
      width: 100,
      child: DropdownButton<String>(
        isExpanded: true,
        value: dropdownValue,
        icon: Icon(Icons.arrow_downward),
//        iconSize: 24,
//        elevation: 16,
        style: Theme.of(context).textTheme.subtitle1,
        underline: Container(
          height: 2,
          color: Theme.of(context).colorScheme.primary,
        ),
        onChanged: (String newValue) {
          setState(() {
            dropdownValue = newValue;
          });
          bloc.updateValue(newValue);
        },
        items: options.map<DropdownMenuItem<String>>((String value) {
          return DropdownMenuItem<String>(
            value: value,
            child: Text(value),
          );
        }).toList(),
      )
    );
  }
}
