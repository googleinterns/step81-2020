import 'dart:collection';
import 'package:flutter/cupertino.dart';
import 'package:macrobaseapp/model/entities/macro.dart';

class MacroNotifier with ChangeNotifier {
  List<Macro> _macroList = [];

  String userEmail;
  Macro _currentMacro;

  UnmodifiableListView<Macro> get macroList => UnmodifiableListView(_macroList);

  Macro get currentMacro => _currentMacro;

  set macroList(List<Macro> macroList) {
    _macroList = macroList;
    notifyListeners();
  }

  set currentMacro(Macro macro) {
    _currentMacro = macro;
    notifyListeners();
  }

  addMacro(Macro macro) {
    _macroList.insert(0, macro);
    notifyListeners();
  }

  deleteMacro(Macro macro) {
    _macroList.removeWhere((_macro) => _macro.macroName  == macro.macroName);
    notifyListeners();
  }
}