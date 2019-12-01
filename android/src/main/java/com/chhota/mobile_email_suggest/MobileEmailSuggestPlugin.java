package com.chhota.mobile_email_suggest;

import android.accounts.AccountManager;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;

import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.google.android.gms.common.AccountPicker;
import com.google.android.gms.common.api.GoogleApiClient;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/** MobileEmailSuggestPlugin */
public class MobileEmailSuggestPlugin implements MethodCallHandler, PluginRegistry.ActivityResultListener {
    private static final int RESOLVE_HINT = 245;
    private static final int REQUEST_CODE_EMAIL = 248;
    /** Plugin registration. */

  private Activity context;
  private Result mResult;

  public static void registerWith(Registrar registrar) {
    final MethodChannel channel = new MethodChannel(registrar.messenger(), "mobile_email_suggest");
    channel.setMethodCallHandler(new MobileEmailSuggestPlugin(registrar, channel));
  }

  public  MobileEmailSuggestPlugin(Registrar registrar, MethodChannel methodChannel){
      this.context = registrar.activity();
      methodChannel.setMethodCallHandler(this);
      registrar.addActivityResultListener(this);
  }

  @Override
  public void onMethodCall(MethodCall call, Result result) {
    if(call.method.equals("getMobileNumber")){
        mResult = result;
        openMobileNumberPickerPopup();
    }else if(call.method.equals("getEmailId")){
        mResult = result;
        getEmailIdPopup();
    }else {
      result.notImplemented();
    }
  }

  private void getEmailIdPopup(){
      Intent googlePicker = AccountPicker.newChooseAccountIntent(null, null,
              new String[]{GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE}, true, null, null, null, null);
      context.startActivityForResult(googlePicker, REQUEST_CODE_EMAIL);
  }

  private void openMobileNumberPickerPopup(){
    GoogleApiClient apiClient = new GoogleApiClient.Builder(context)
                    .addApi(Auth.CREDENTIALS_API).build();

    HintRequest hintRequest = new HintRequest.Builder()
            .setPhoneNumberIdentifierSupported(true)
            .build();

    PendingIntent intent = Auth.CredentialsApi.getHintPickerIntent(
            apiClient, hintRequest
    );
    try {
      context.startIntentSenderForResult(
              intent.getIntentSender(),
              RESOLVE_HINT, null, 0, 0, 0
      );
    } catch (IntentSender.SendIntentException e) {
      e.printStackTrace();
    }
  }


  @Override
  public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
      if (requestCode == RESOLVE_HINT) {
          if (resultCode == Activity.RESULT_OK) {
              Credential credential = data.getParcelableExtra(Credential.EXTRA_KEY);
              mResult.success(credential.getId());
              return true;
          }
      }else if(requestCode == REQUEST_CODE_EMAIL && resultCode == Activity.RESULT_OK){
          String emaildId = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
          mResult.success(emaildId);
          return true;
      }
      return false;
  }
}
