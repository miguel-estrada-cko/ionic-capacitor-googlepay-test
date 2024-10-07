import { registerPlugin, Capacitor } from '@capacitor/core';
import { WebPlugin } from '@capacitor/core';
import { GooglePayPlugin } from './google-pay';

declare var google: any;

export class GooglePayWeb extends WebPlugin implements GooglePayPlugin {

   async presentPaymentSheet(): Promise<{ success: boolean, token: string }> {

    console.log('CKO, Web: presentPaymentSheet');

    const paymentsClient = new google.payments.api.PaymentsClient({
      environment: 'TEST'
    });

    const isReadyToPayRequest = {
      apiVersion: 2,
      apiVersionMinor: 0,
      allowedPaymentMethods: [
        {
          type: 'CARD',
          parameters: {
            allowedAuthMethods: ['PAN_ONLY', 'CRYPTOGRAM_3DS'],
            allowedCardNetworks: ['MASTERCARD', 'VISA'],
          },
          tokenizationSpecification: {
            type: 'PAYMENT_GATEWAY',
            parameters: {
              gateway: 'checkoutltd',  // Aquí va el nombre del gateway
              gatewayMerchantId: 'pk_sbox_baboim557qv7msgzftq4jjaxnuv',  // ID del comerciante del gateway
            },
          },
        },
      ],
    };

    const isReadyToPay = await paymentsClient.isReadyToPay(isReadyToPayRequest);
    if (isReadyToPay.result) {
      const paymentRequest = {
        apiVersion: 2,
        apiVersionMinor: 0,
        allowedPaymentMethods: isReadyToPayRequest.allowedPaymentMethods,
        transactionInfo: {
          totalPriceStatus: 'FINAL',
          totalPrice: '10.00',
          currencyCode: 'USD',
        },
        merchantInfo: {
          merchantName: 'Example Merchant',
        },
      };

      const paymentData = await paymentsClient.loadPaymentData(paymentRequest);
      return { success: true, token: paymentData.paymentMethodData.tokenizationData.token };
    } else {
      throw new Error('Google Pay no disponible');
    }
  }
}


const GooglePayPluginWeb = registerPlugin<GooglePayPlugin>('GooglePayPlugin');

export default GooglePayPluginWeb;
