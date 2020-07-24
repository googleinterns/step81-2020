import 'dart:convert';

import 'package:flutter_test/flutter_test.dart';
import 'package:macrobaseapp/model/adapters/action_model.dart';
import 'package:macrobaseapp/model/adapters/macro_model.dart';
import 'package:macrobaseapp/model/adapters/trigger_model.dart';
import 'package:macrobaseapp/model/entities/macro.dart';

import '../../fixtures/fixture_reader.dart';

void main() {

  final tMacroCommandPollModel = MacroModel(
    macroName: "Weekly Game Night Poll",
    description: "A recurring poll bot that allows the team to vote on which game to play",
    creatorId: "tonyshen@google.com",
    trigger: CommandTriggerModel(
        command: "make game poll"
    ),
//    action: PollActionModel(
//      question: "What game should we play tomorrow ? ",
//      choices: ["Saboteur", "Pubg", "Code name"],
//      userCanAddOptions: true,
//      userCanVoteMultiple: false,
//    ),
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
        json.decode(fixture('macro_command_poll.json'));
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
        expect(result, json.decode(fixture('macro_command_poll.json')));
      },
    );
  });

}
