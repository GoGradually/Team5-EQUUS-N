import MediumButton from '../../../components/buttons/MediumButton';
import Modal, { ModalType } from '../../../components/modals/Modal';
import { hideModal } from '../../../utility/handleModal';

export default function ScheduleDeleteModal({ deleteSchedule, onClose }) {
  return (
    <Modal
      type={ModalType.DOUBLE}
      title='일정을 삭제하시겠어요?'
      subButton={
        <MediumButton
          text='취소'
          disabled={true}
          onClick={() => {
            hideModal();
          }}
        />
      }
      mainButton={
        <MediumButton
          text='삭제'
          isOutlined={false}
          onClick={() => {
            hideModal();
            deleteSchedule();
            onClose();
          }}
        />
      }
    />
  );
}
