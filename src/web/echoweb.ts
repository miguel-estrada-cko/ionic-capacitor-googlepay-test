import { registerPlugin } from '@capacitor/core';
import { WebPlugin } from '@capacitor/core';
import { EchoPlugin } from './echo';

export class EchoWeb extends WebPlugin implements EchoPlugin {
  async echo(options: { value: string }): Promise<{ value: string }> {
    console.log('CKO, Web:', options.value);
    return options; // Devuelve el valor tal como se recibe, simulando el comportamiento del plugin.
  }
}
