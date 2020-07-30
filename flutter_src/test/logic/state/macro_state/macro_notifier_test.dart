import 'package:flutter_test/flutter_test.dart';
import 'package:macrobaseapp/logic/state/macro_state/macro_notifier.dart';
import 'package:macrobaseapp/model/entities/macro.dart';

void main() {
  test(
    'Setting current team changes current team',
    () async {
      MacroNotifier macroNotifier = MacroNotifier();
      Macro macro = Macro();

      macroNotifier.currentMacro = macro;

      expect(macroNotifier.currentMacro, equals(macro));
    },
  );
}
