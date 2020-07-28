import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:macrobaseapp/logic/state/macro_state/macro_notifier.dart';
import 'package:macrobaseapp/model/adapters/macro_model.dart';
import 'package:macrobaseapp/model/entities/macro.dart';

class FirestoreService {
  final Firestore db;

  FirestoreService({FirestoreService db})
    : db = db ?? Firestore.instance;

  void getMacros(MacroNotifier macroNotifier, String creatorId) async {
    QuerySnapshot snapshot = await db.collection('macros').where("creatorId", isEqualTo: creatorId).getDocuments();

    List<Macro> _macroList = [];

    snapshot.documents.forEach((element) {
      try{
        Macro macro = MacroModel.fromJson(element.data);
        macro.macroId = element.documentID;
        _macroList.add(macro);
      } catch(e) {
        print(element.data);
      }
    });

    macroNotifier.macroList = _macroList;
  }


  Future<void> removeObject(String collectionName, String macroId) {
    return db.collection(collectionName).document(macroId).delete();
  }


  void uploadObject(String collectionName, Map<String, dynamic> json) {
    db.collection(collectionName).document()
        .setData(json);
  }


  Future<List<Macro>> queryMacro(String macroName) async {
    QuerySnapshot snapshot = await db.collection('macros')
        .where("macroName", isEqualTo: macroName)
        .getDocuments();

    List<Macro> _macroList = [];

    snapshot.documents.forEach((element) {
      Macro macro = MacroModel.fromJson(element.data);
      _macroList.add(macro);
    });

    return _macroList;
  }
}



