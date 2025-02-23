import React, { StrictMode } from 'react';
import ReactDOM from 'react-dom/client';
import App from './App';
import { registerSW } from 'virtual:pwa-register';

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

ReactDOM.createRoot(document.getElementById('root')).render(
  <StrictMode>
    <App />
  </StrictMode>,
);
