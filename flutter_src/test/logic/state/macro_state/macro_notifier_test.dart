import 'package:flutter_test/flutter_test.dart';
import 'package:macrobaseapp/logic/state/macro_state/macro_notifier.dart';
import 'package:macrobaseapp/model/entities/macro.dart';

void main() {
  test(
    'Setting current Macro changes current Macro',
    () async {
      MacroNotifier macroNotifier = MacroNotifier();
      Macro macro = Macro();

      macroNotifier.currentMacro = macro;

      expect(macroNotifier.currentMacro, equals(macro));
    },
  );

  test(
    'Deleting Macro deletes the macro from Notifier',
    () async {
      MacroNotifier macroNotifier = MacroNotifier();
      Macro macro = Macro();

      macroNotifier.macroList = [macro];
      macroNotifier.deleteMacro(macro);

      expect(macroNotifier.macroList, equals([]));
    },
  );
}
