/// <reference types="vite/client" />

interface ImportMeta {
  readonly env: ImportMetaEnv;
}

interface ImportMetaEnv {
  readonly VITE_APP_NAME: string;
  readonly VITE_BASE_PATH: string;
  readonly VITE_BASE_API_PORT: number;
  readonly VITE_APP_TITLE: string;
  // 更多环境变量...
}
