import { registerPlugin } from '@capacitor/core';
import { WebPlugin } from '@capacitor/core';

export interface GooglePayPlugin {
  presentPaymentSheet(): Promise<{ success: boolean, token: string }>;
}

const GooglePay = registerPlugin<GooglePayPlugin>('GooglePay', {
    web: () => import('./google-payweb').then(m => new m.GooglePayWeb()),
});


export default GooglePay;