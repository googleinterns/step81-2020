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
        child: Center(
          child: Column(
            mainAxisSize: MainAxisSize.max,
            mainAxisAlignment: MainAxisAlignment.center,
            children: <Widget>[
              Text(
                "Macrobase",
                style: Theme.of(context).textTheme.headline4,
              ),
              Text(
                "Automate the boring stuff.",
                style: Theme.of(context).textTheme.subtitle1,
              ),
              Image(
                image: AssetImage("clip-problem-solving.png"),
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
