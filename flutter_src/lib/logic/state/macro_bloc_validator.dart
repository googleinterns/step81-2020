import 'package:macrobaseapp/logic/usecases/macro_firestore/firestore_macro_operation.dart';
import 'package:macrobaseapp/model/entities/macro.dart';

class CustomBlocValidator {
  static Future<String> nameValidator(String name) async {
    //Avoid too many Firestore calls
    await Future.delayed(Duration(milliseconds: 200));
    List<Macro> list = await queryMacro(name);
    if (list.length > 0)
      return "Sorry, this macro name already exist";
    else
      return null;
  }

  static String sheetUrlValidator(String string) {
    final sheetUrlRegExp =
    RegExp("https:\/\/docs.google.com\/spreadsheets\/d\/.*\/edit#gid=.*");

    if (string == null || string.isEmpty || sheetUrlRegExp.hasMatch(string)) {
      return null;
    }
    return "Sheet URL Format - Validator Error";
  }
}