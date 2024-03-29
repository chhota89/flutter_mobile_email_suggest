import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:mobile_email_suggest/mobile_email_suggest.dart';

void main() {
  const MethodChannel channel = MethodChannel('mobile_email_suggest');

  setUp(() {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return '42';
    });
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });

  test('getPlatformVersion', () async {
    expect(await MobileEmailSuggest.platformVersion, '42');
  });
}
