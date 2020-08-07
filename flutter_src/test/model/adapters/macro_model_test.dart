import 'dart:convert';

import 'package:flutter_test/flutter_test.dart';
import 'package:macrobaseapp/model/adapters/action_model.dart';
import 'package:macrobaseapp/model/adapters/macro_model.dart';
import 'package:macrobaseapp/model/adapters/trigger_model.dart';
import 'package:macrobaseapp/model/entities/action.dart';
import 'package:macrobaseapp/model/entities/macro.dart';

import '../../fixtures/fixture_reader.dart';

void main() {

  final tMacroCommandPollModel = MacroModel(
    macroName: "Daily",
    description: "Daily Check-in Bot records your teams' daily updates in a Google sheet",
    creatorId: "tonyshen@google.com",
    trigger: CommandTriggerModel(
      command: "123"
    ),
    action: SheetAppendActionModel(
      sheetUrl: "https://docs.google.com/spreadsheets/d/YOUR_SHEET_ID_HERE/edit#gid=0",
      columnValue: [],
    )
  );

  test(
    'should be a subclass of Macro entity',
        () async {
      expect(tMacroCommandPollModel, isA<Macro>());
    },
  );

  group('fromJson', () {
    test(
      'should return a valid model the Macro uses command based trigger and poll actions',
          () async {
        //arrange
        final Map<String, dynamic> jsonMap =
        json.decode(fixture('macro_sheet_append_command.json'));
        //act
        final result = MacroModel.fromJson(jsonMap);
        //assert
        expect(result, equals(tMacroCommandPollModel));
      },
    );
  });

  group('toJson', () {
    test(
      'should return a Json map containing the proper data',
          () async {
        final result = tMacroCommandPollModel.toJson();
        expect(result, json.decode(fixture('macro_sheet_append_command.json')));
      },
    );
  });

}
