import React, { StrictMode } from 'react';
import ReactDOM from 'react-dom/client';
import App from './App';
import { registerSW } from 'virtual:pwa-register';

// async function enableMocking() {
//   if (process.env.NODE_ENV !== 'development') {
//     return;
//   }

//   const { worker } = await import('./mocks/browser');

//   // `worker.start()` returns a Promise that resolves
//   // once the Service Worker is up and ready to intercept requests.
//   return worker.start({
//     onUnhandledRequest: 'bypass', // handler에 없는 요청은 처리 안함
//   });
// }

// 5초마다 업데이트 확인
const intervalMS = 5 * 1000;
const updateSW = registerSW({
  onRegisteredSW(swUrl, registration) {
    registration &&
      setInterval(() => {
        registration.update();
      }, intervalMS);
  },
  immediate: false,
});
updateSW();

// enableMocking().then(() => {
ReactDOM.createRoot(document.getElementById('root')).render(
  <StrictMode>
    <App />
  </StrictMode>,
);
// });
