import 'package:macrobaseapp/logic/api/firestore_db.dart';
import 'package:macrobaseapp/model/entities/macro.dart';

class CustomBlocValidator {
  static Future<bool> nameValidator(String name) async {
    FirestoreService db = FirestoreService();

    //Avoid too many Firestore calls
    await Future.delayed(Duration(milliseconds: 500));
    List<Macro> list = await db.queryMacro(name);
    if (list.length > 0) {
      return false;
    }
    return true;
  }

  static bool oneWordValidator(String string) {
    if (string.contains(" ")) {
      return false;
    }
    return true;
  }

  static bool urlValidator(String string) {
    bool isValidUrl = Uri.parse(string).isAbsolute;
    if (!isValidUrl) {
      return false;
    }
    return true;
  }

  static bool sheetUrlValidator(String string) {
    final sheetUrlRegExp =
        RegExp("https:\/\/docs.google.com\/spreadsheets\/d\/.*\/edit#gid=.*");

    if (!sheetUrlRegExp.hasMatch(string)) {
      return false;
    }
    return true;
  }

  static bool commaSeperatedEmailValidator(String string) {
    List<String> errors = [];

    string = string.trim();
    if (string.length == 0) return true;

    List<String> emails = string.split(',');

    for (int i = 0; i < emails.length; i++) {
      bool emailValid = RegExp(
              r"^[a-zA-Z0-9.a-zA-Z0-9.!#$%&'*+-/=?^_`{|}~]+@[a-zA-Z0-9]+\.[a-zA-Z]+")
          .hasMatch(emails[i]);
      if (!emailValid) {
        return false;
      }
    }

    return true;
  }
}
