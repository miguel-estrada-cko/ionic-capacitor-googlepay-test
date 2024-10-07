import { Component } from '@angular/core';
import GooglePay from '../../web/google-pay';
import Echo from '../../web/echo';

@Component({
  selector: 'app-tab1',
  templateUrl: 'tab1.page.html',
  styleUrls: ['tab1.page.scss']
})
export class Tab1Page {

  constructor() {
    this.load();
  }

  async load() {
    const { value } = await Echo.echo({ value: 'Hello...' });
  }

  async presentGooglePay() {
    try {
      console.log("CKO, presentGooglePay")
      const result = await GooglePay.presentPaymentSheet();

      console.log('CKO, Pago exitoso', result);
    } catch (error) {
      console.error('CKO, Error en el pago', error);
    }
  }

}
