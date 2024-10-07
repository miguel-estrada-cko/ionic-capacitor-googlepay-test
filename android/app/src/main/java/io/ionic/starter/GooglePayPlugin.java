package io.ionic.starter;

import android.app.Activity;
import android.content.IntentSender;
import android.util.Log;

import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.wallet.PaymentData;
import com.google.android.gms.wallet.PaymentDataRequest;
import com.google.android.gms.wallet.PaymentsClient;
import com.google.android.gms.wallet.Wallet;
import com.google.android.gms.wallet.WalletConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@CapacitorPlugin(name = "GooglePay")
public class GooglePayPlugin extends Plugin {

  private static final int PAYMENT_REQUEST_CODE = 1;
  private PaymentsClient paymentsClient;

  @Override
  public void load() {
    Log.d("CKO", "Android: load()");
    Activity activity = getActivity();
    paymentsClient = Wallet.getPaymentsClient(
      activity,
      new Wallet.WalletOptions.Builder()
        .setEnvironment(WalletConstants.ENVIRONMENT_TEST) // Cambiar a ENVIRONMENT_PRODUCTION en producción
        .build()
    );
  }

  @PluginMethod
  public void presentPaymentSheet(PluginCall call) {
    Log.d("CKO", "Android: presentPaymentSheet");
    PaymentDataRequest request = createPaymentDataRequest();

    Task<PaymentData> task = paymentsClient.loadPaymentData(request);
    task.addOnCompleteListener(taskEvent -> {
      if (taskEvent.isSuccessful()) {
        Log.e("CKO", "Pago exitoso");
        PaymentData paymentData = taskEvent.getResult();
      } else {
        Exception e = taskEvent.getException();
        if (e instanceof ResolvableApiException) {
          try {
            ResolvableApiException resolvable = (ResolvableApiException) e;
            resolvable.startResolutionForResult(getActivity(), PAYMENT_REQUEST_CODE);
            Log.e("CKO", "2nd activity");
          } catch (IntentSender.SendIntentException sendEx) {
            Log.e("CKO", "Error al iniciar la resolución", sendEx);
          }
        } else {
          Log.e("CKO", "Error en el pago", e);
        }
      }
    });
  }

  private PaymentDataRequest createPaymentDataRequest() {
    JSONObject paymentDataRequestJson = getPaymentDataRequest();
    return PaymentDataRequest.fromJson(paymentDataRequestJson.toString());
  }

  private JSONObject getPaymentDataRequest() {
    JSONObject paymentDataRequest = new JSONObject();
    try {
      paymentDataRequest.put("apiVersion", 2);
      paymentDataRequest.put("apiVersionMinor", 0);

      JSONArray allowedPaymentMethods = new JSONArray();
      JSONObject cardPaymentMethod = new JSONObject();
      cardPaymentMethod.put("type", "CARD");

      JSONObject cardParams = new JSONObject();
      cardParams.put("allowedAuthMethods", new JSONArray()
        .put("PAN_ONLY")
        .put("CRYPTOGRAM_3DS"));
      cardParams.put("allowedCardNetworks", new JSONArray()
        .put("MASTERCARD")
        .put("VISA"));

      JSONObject tokenizationSpecification = new JSONObject();
      tokenizationSpecification.put("type", "PAYMENT_GATEWAY");
      tokenizationSpecification.put("parameters", new JSONObject()
        .put("gateway", "checkoutltd")
        .put("gatewayMerchantId", "pk_sbox_baboim557qv7msgzftq4jjaxnuv"));

      cardPaymentMethod.put("parameters", cardParams);
      cardPaymentMethod.put("tokenizationSpecification", tokenizationSpecification);

      allowedPaymentMethods.put(cardPaymentMethod);
      paymentDataRequest.put("allowedPaymentMethods", allowedPaymentMethods);

      JSONObject merchantInfo = new JSONObject();
      merchantInfo.put("merchantId", "12345678901234567890");
      merchantInfo.put("merchantName", "Estrada & Co.");
      paymentDataRequest.put("merchantInfo", merchantInfo);

      JSONObject transactionInfo = new JSONObject();
      transactionInfo.put("totalPriceStatus", "FINAL");
      transactionInfo.put("totalPrice", "10.00");
      transactionInfo.put("currencyCode", "USD");
      transactionInfo.put("countryCode", "US");
      paymentDataRequest.put("transactionInfo", transactionInfo);

    } catch (JSONException e) {
      Log.e("GooglePayPlugin", "Error al crear PaymentDataRequest", e);
    }

    return paymentDataRequest;
  }

  private void handlePaymentSuccess(PaymentData paymentData) {
    Log.e("CKO", "Pago exitoso");
    String json = paymentData.toJson();
  }


}
