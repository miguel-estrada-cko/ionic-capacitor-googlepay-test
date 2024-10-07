import { registerPlugin } from '@capacitor/core';
import { WebPlugin } from '@capacitor/core';

export interface EchoPlugin {
    echo(options: { value: string }): Promise<{ value: string }>;
}


const Echo = registerPlugin<EchoPlugin>('Echo', {
    web: () => import('./echoweb').then(m => new m.EchoWeb()),
});


export default Echo;