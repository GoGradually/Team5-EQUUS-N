import { useEffect, useState } from 'react';
import CustomInput from '../../components/CustomInput';
import NavBar2 from '../../components/NavBar2';
import Icon from '../../components/Icon';
import LargeButton from '../../components/buttons/LargeButton';
import { useLocation, useNavigate, useParams } from 'react-router-dom';

import { showToast } from '../../utility/handleToast';
import { checkTeamSpaceMakingInfo } from '../../utility/inputChecker';
import CustomDatePicker, {
  DatePickerDropdown,
} from '../../components/CustomDatePicker';
import Modal from '../../components/modals/Modal';
import MediumButton from '../../components/buttons/MediumButton';
import { hideModal, showModal } from '../../utility/handleModal';
import { useEditTeam, useLeaveTeam } from '../../api/useTeamspace';
import { reSelectTeam } from './components/MemberElement';
import { useTeam } from '../../useTeam';

/**
 * @param {object} props
 * @param {boolean} props.isFirst
 * @returns
 */
export default function TeamSpaceEdit({ isFirst = false }) {
  const location = useLocation();
  const { teamId } = useParams();
  const [team, setTeam] = useState({
    name: '',
    startDate: new Date(),
    endDate: new Date(),
    feedbackType: 'ANONYMOUS',
  });
  const [isDatePickerOpen1, setIsDatePickerOpen1] = useState(false);
  const [isDatePickerOpen2, setIsDatePickerOpen2] = useState(false);
  const [canDelete, setCanDelete] = useState(false);
  const { mutate: editTeam } = useEditTeam(teamId);
  const { mutate: leaveTeam } = useLeaveTeam(teamId);
  const { selectedTeam, removeSelectedTeam } = useTeam();

  const navigate = useNavigate();

  const handleDeleteButton = () => {
    if (canDelete) {
      showModal(deleteTeamModal);
    } else {
      showToast('남은 팀원이 있어 삭제할 수 없어요');
    }
  };

  const handleConfirmDeleteTeam = () => {
    leaveTeam(null, {
      onSuccess: () => {
        hideModal();
        reSelectTeam(teamId, selectedTeam, removeSelectedTeam);
        navigate(-2);
      },
    });
  };

  const deleteTeamModal = (
    <Modal
      type='DOUBLE'
      title={`팀 스페이스를 정말 삭제할까요?`}
      mainButton={
        <MediumButton
          text='삭제'
          isOutlined={false}
          onClick={handleConfirmDeleteTeam}
        />
      }
      subButton={
        <MediumButton
          text='취소'
          disabled={true}
          isOutlined={true}
          onClick={() => {
            hideModal();
          }}
        />
      }
    />
  );

  const onClickNext = () => {
    if (!checkTeamSpaceMakingInfo(team.name, team.startDate, team.endDate)) {
      return;
    } else {
      editTeam(team);
      navigate(-1);
    }
  };

  const onClickPop = () => {
    navigate(-1);
  };

  const handleToggleAnonymous = () => {
    setTeam({
      ...team,
      feedbackType:
        team.feedbackType === 'IDENTIFIED' ? 'ANONYMOUS' : 'IDENTIFIED',
    });
  };

  useEffect(() => {
    if (location.state) {
      setTeam(location.state?.teamResponse);
      setCanDelete(location.state?.members?.length < 2);
    }
  }, []);

  useEffect(() => {
    if (team) {
      if (team.startDate > team.endDate) {
        setEndDate(team.startDate);
        showToast('종료일은 시작일보다 빠를 수 없습니다');
      }
    }
  }, [team.startDate, team.endDate]);

  return (
    <div className='relative flex h-dvh w-full flex-col justify-start'>
      <NavBar2
        canPop={false}
        title='팀 스페이스 수정하기'
        canClose={true}
        isCloseLeft={true}
        onClickClose={() => {
          onClickPop();
        }}
      />
      <div className={`h-${isFirst ? '2' : '4'}`} />
      <CustomInput
        label='팀 이름'
        content={team.name}
        setContent={(name) => setTeam({ ...team, name: name })}
      />
      <div className='h-6' />
      <div className='flex flex-col gap-2'>
        {/* 제목 */}
        {<p className='subtitle-2 text-gray-0'>프로젝트 기간</p>}
        {/* 인풋 */}
        <div className='flex'>
          <CustomDatePicker
            date={new Date(team.startDate)}
            setDate={(date) => setTeam({ ...team, startDate: date })}
            customInput={
              <DatePickerDropdown
                isStartTime={true}
                isPickerOpen={isDatePickerOpen1}
              />
            }
            setIsPickerOpen={setIsDatePickerOpen1}
          />
          <div className='w-3 shrink-0' />
          <CustomDatePicker
            date={new Date(team.endDate)}
            setDate={(date) => setTeam({ ...team, endDate: date })}
            customInput={
              <DatePickerDropdown
                isStartTime={false}
                isPickerOpen={isDatePickerOpen2}
              />
            }
            setIsPickerOpen={setIsDatePickerOpen2}
          />
        </div>
      </div>
      <div className='h-6' />
      <CustomInput
        label='피드백 방식'
        isOutlined={false}
        content='익명을 기본으로 설정할게요'
        addOn={
          <button
            className='flex h-full w-full items-center justify-center'
            onClick={handleToggleAnonymous}
          >
            <Icon
              name={
                team.feedbackType === 'ANONYMOUS' ?
                  'checkBoxClick'
                : 'checkBoxNone'
              }
            />
          </button>
        }
      />
      <p className='caption-1 mt-3 text-gray-300'>
        피드백 작성 시 피드백 방식을 변경할 수 있어요!
      </p>

      {/* 다음 버튼 */}
      <div className='absolute right-0 bottom-[34px] left-0 flex flex-col bg-gray-900'>
        <LargeButton
          text='완료'
          isOutlined={false}
          onClick={() => {
            onClickNext();
          }}
        />
        <div className='h-3' />
        <div
          className={
            'rounded-300 flex h-[56px] w-full items-center justify-center px-4 py-2 text-gray-300'
          }
        >
          <a onClick={() => handleDeleteButton()}>팀 스페이스 삭제</a>
        </div>
      </div>
    </div>
  );
}
