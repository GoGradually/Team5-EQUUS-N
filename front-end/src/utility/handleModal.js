import { createRoot } from 'react-dom/client';

let modalRoot = null;

/**
 * 모달을 띄우는 함수
 * @param {ReactElement} reactElement
 */
export function showModal(reactElement) {
  const modal = document.querySelector('dialog');

  // 'dialog' 요소를 루트로 지정하여, 인자로 받은 reactElement를 루트에 바로 하위에 렌더링
  if (modal) {
    // 이미 root 객체가 생성됐는데 또 생성하면 warning이 발생하므로, 하나만 생성하도록 제한
    if (!modalRoot) {
      modalRoot = createRoot(modal);
    }
    modalRoot.render(reactElement);
    modal.showModal();
  }
}

/**
 * 모달을 숨기는 함수
 */
export function hideModal() {
  const modal = document.querySelector('dialog');

  if (modal) {
    modal.close();
  }
}
