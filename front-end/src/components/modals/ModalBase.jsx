import { useRef } from 'react';
import { hideModal } from '../../utility/handleModal';

export default function ModalBase() {
  const dialogRef = useRef(null);

  return (
    <dialog
      ref={dialogRef}
      onClick={(event) =>
        dialogRef.current && dialogRef.current === event.target && hideModal()
      }
      className='m-auto w-full max-w-[400px] bg-transparent px-5 outline-0 transition-all duration-300 *:mx-auto backdrop:bg-black/60 backdrop:backdrop-blur-xs open:opacity-100 starting:open:opacity-0'
    />
  );
}
