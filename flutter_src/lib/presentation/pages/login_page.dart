import 'package:flutter/material.dart';
import 'package:macrobaseapp/presentation/widgets/google_signin_button.dart';

class LoginPage extends StatelessWidget {
  const LoginPage({Key key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return LoginPageBody._();
  }
}

class LoginPageBody extends StatelessWidget {
  const LoginPageBody._({Key key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Container(
        color: Colors.white,
        child: Center(
          child: Column(
            mainAxisSize: MainAxisSize.max,
            mainAxisAlignment: MainAxisAlignment.center,
            children: <Widget>[
              Text(
                "Macrobase",
                style: TextStyle(
                  fontSize: 40,
                  fontWeight: FontWeight.bold,
                  color: Colors.green,
                ),
              ),
              Text(
                "Automate the boring stuff.",
                style: TextStyle(
                  fontSize: 20,
                  color: Colors.green,
                ),
              ),
              Image(
                image: AssetImage("automate_task.png"),
                height: 400,
              ),
              SizedBox(height: 30),
              signInButton(context),
            ],
          ),
        ),
      ),
    );
  }
}
