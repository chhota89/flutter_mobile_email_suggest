import 'dart:async';

import 'package:flutter/services.dart';

class MobileEmailSuggest {
  static const MethodChannel _channel =
      const MethodChannel('mobile_email_suggest');

  static Future<String> get getMobileNumber async {
    final String version = await _channel.invokeMethod('getMobileNumber');
    return version;
  }

  static Future<String> get getEmailId async {
    final String version = await _channel.invokeMethod('getEmailId');
    return version;
  }
}
