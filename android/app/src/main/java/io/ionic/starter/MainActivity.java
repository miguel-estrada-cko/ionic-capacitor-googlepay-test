package io.ionic.starter;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.getcapacitor.BridgeActivity;
import com.google.android.gms.wallet.PaymentData;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends BridgeActivity {
  private static final int PAYMENT_REQUEST_CODE = 1;

  public void onCreate(Bundle savedInstanceState) {
    Log.d("CKO", "onCreate");
    registerPlugin(EchoPlugin.class);
    registerPlugin(GooglePayPlugin.class);
    super.onCreate(savedInstanceState);
  }

  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == PAYMENT_REQUEST_CODE) {
      if (resultCode == Activity.RESULT_OK) {
        PaymentData paymentData = PaymentData.getFromIntent(data);

        String json = paymentData.toJson();

        try {
          // Crear un JSONObject desde la cadena JSON
          JSONObject paymentDataJson = new JSONObject(json);

          // Acceder al token
          String token = paymentDataJson.getJSONObject("paymentMethodData")
            .getJSONObject("tokenizationData")
            .getString("token");
          Log.e("CKO", "Android Pago exitoso: " + token);
        } catch (JSONException e) {
          throw new RuntimeException(e);
        }
      } else {
        // El usuario no completó el pago
        Log.e("CKO", "Pago no completado.");
      }
    }
  }
}
