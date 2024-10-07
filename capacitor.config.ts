import type { CapacitorConfig } from '@capacitor/cli';

const config: CapacitorConfig = {
  appId: 'io.ionic.starter',
  appName: 'pay-plugin',
  webDir: 'www',
  plugins: {
    GooglePayPlugin: {
      className: "GooglePayPlugin",
      key: 'value',
      // Puedes incluir cualquier configuración personalizada para el plugin aquí si es necesario.
    }
  }
};

export default config;
