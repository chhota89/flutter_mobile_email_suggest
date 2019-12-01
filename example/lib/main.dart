import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:mobile_email_suggest/mobile_email_suggest.dart';

void main() => runApp(MyApp());

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _mobileNumber = 'Unknown';
  String _emailId = 'Unknown';

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> getMobileNumber() async {
    String platformVersion;
    // Platform messages may fail, so we use a try/catch PlatformException.
    try {
      platformVersion = await MobileEmailSuggest.getMobileNumber;
    } on PlatformException {
      platformVersion = 'Failed to get platform version.';
    }

    // If the widget was removed from the tree while the asynchronous platform
    // message was in flight, we want to discard the reply rather than calling
    // setState to update our non-existent appearance.
    if (!mounted) return;

    setState(() {
      _mobileNumber = platformVersion;
    });
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> getEmailId() async {
    String emailId;
    // Platform messages may fail, so we use a try/catch PlatformException.
    try {
      emailId = await MobileEmailSuggest.getEmailId;
    } on PlatformException {
      emailId = 'Failed to get email id.';
    }

    // If the widget was removed from the tree while the asynchronous platform
    // message was in flight, we want to discard the reply rather than calling
    // setState to update our non-existent appearance.
    if (!mounted) return;

    setState(() {
      _emailId = emailId;
    });
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.center,
            mainAxisAlignment: MainAxisAlignment.center,
            children: <Widget>[
              RaisedButton(
                  child: Text("Get Mobile Number"),
                  onPressed: () {
                    getMobileNumber();
                  }),
              Text("Mobile Number $_mobileNumber"),
              RaisedButton(
                  child: Text("Get Email Id"),
                  onPressed: () {
                    getEmailId();
                  }),
              Text("Email Id $_emailId"),
            ],
          ),
        ),
      ),
    );
  }
}
