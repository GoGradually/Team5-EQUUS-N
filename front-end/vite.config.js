import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';
import svgr from '@svgr/rollup';
import tailwindcss from '@tailwindcss/vite';
import { VitePWA } from 'vite-plugin-pwa';

// https://vite.dev/config/
export default defineConfig({
  plugins: [
    react(),
    tailwindcss(),
    svgr(),
    VitePWA({
      strategies: 'injectManifest',
      filename: 'service-worker.js',
      injectManifest: {
        swSrc: 'public/service-worker.js',
        maximumFileSizeToCacheInBytes: 5 * 1024 * 1024,
      },
      manifest: {
        name: '피드한줌',
        short_name: '피드한줌',
        start_url: '/',
        display: 'standalone',
        background_color: '#191919',
        theme_color: '#ffffff',
        icons: [
          {
            src: '/logo.png',
            sizes: '188x188',
            type: 'image/png',
          },
        ],
      },
      workbox: {
        maximumFileSizeToCacheInBytes: 5 * 1024 * 1024,
      },
      registerType: 'autoUpdate', // 서비스 워커 업데이트 설정
      devOptions: {
        enabled: true,
        type: 'module',
      },
      // 추가 캐싱 전략 등 원하는 옵션 추가 가능
    }),
  ],
});
