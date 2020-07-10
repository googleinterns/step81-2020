// Button Adapted from: https://medium.com/flutter-community/flutter-implementing-google-sign-in-71888bca24ed
import 'package:flutter/material.dart';
import 'package:macrobaseapp/logic/usecases/login/firebase_auth.dart';
import 'package:provider/provider.dart';


Widget signInButton(BuildContext context) {
  return OutlineButton(
    splashColor: Colors.grey,
    onPressed: () {
      context.read<FirebaseAuthService>().signInWithGoogle();
    },
    shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(40)),
    highlightElevation: 0,
    borderSide: BorderSide(color: Colors.grey),
    child: Padding(
      padding: const EdgeInsets.fromLTRB(0, 10, 0, 10),
      child: Row(
        mainAxisSize: MainAxisSize.min,
        mainAxisAlignment: MainAxisAlignment.center,
        children: <Widget>[
          Image(image: AssetImage("assets/google_logo.png"), height: 35.0),
          Padding(
            padding: const EdgeInsets.only(left: 10),
            child: Text(
              'Sign in with Google',
              style: Theme.of(context).textTheme.button,
            ),
          )
        ],
      ),
    ),
  );
}