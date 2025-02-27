import { useEffect, useReducer, useState } from 'react';
import {
  useMainCard,
  useMainCard2,
  useNotification,
} from '../../api/useMainPage';
import Accordion from '../../components/Accordion';
import TeamMatesCard from '../../components/TeamMatesCard';
import StickyWrapper from '../../components/wrappers/StickyWrapper';
import MainCard from './components/MainCard';
import Slider from 'react-slick';
import 'slick-carousel/slick/slick.css';
import 'slick-carousel/slick/slick-theme.css';
import '../../styles/slider.css';
import { filterNotifications } from '../../utility/handleNotification';
import { useLocation, useNavigate } from 'react-router-dom';
import { hideModal, showModal } from '../../utility/handleModal';
import Modal, { ModalType } from '../../components/modals/Modal';
import ProfileImage from '../../components/ProfileImage';
import MediumButton from '../../components/buttons/MediumButton';
import ScheduleAction, {
  ScheduleActionType,
} from '../calendar/components/ScheduleAction';
import TodoAdd from '../calendar/components/TodoAdd';
import { checkIsFinished, getScheduleTimeDiff } from '../../utility/time';
import { useTeam } from '../../store/useTeam';
import useScheduleAction from '../calendar/hooks/useScheduleAction';
import { useUser } from '../../store/useUser';
import useHandlePop from '../../utility/useHandlePop';
import Banner from './components/Banner';
import { handleFreqFeedbackReq } from './components/Alarm';
import OnboardingNotice from './components/OnboardingNotice';
import usePushNoti from '../../api/usePushNoti';
import { motion } from 'motion/react';

export default function MainPage() {
  const location = useLocation();
  const isFirstVisit = location.state?.init ?? false;
  const { setPushNoti, isLoading: waitingAppServerKey } = usePushNoti();
  const searchParams = new URLSearchParams(location.search);
  const redirect = searchParams.get('redirect') ?? null;
  const teamId = searchParams.get('teamId') ?? null;
  const senderId = searchParams.get('senderId') ?? null;
  const scheduleId = searchParams.get('scheduleId') ?? null;
  const scheduleDate = searchParams.get('scheduleDate') ?? null;

  const navigate = useNavigate();
  const [banners, setBanners] = useState();
  const [timeDiff, setTimeDiff] = useState();
  const [isTodoAddOpen, toggleTodoAdd] = useReducer((prev) => !prev, false);
  const [isScheduleOpen, toggleSchedule] = useReducer((prev) => !prev, false);
  const [filteredTeams, setFilteredTeams] = useState([]);

  const { teams, selectedTeam, selectTeam } = useTeam(true);
  const { userId } = useUser();
  const { data: recentScheduleData, isPending: isMainCardPending } =
    useMainCard(selectedTeam);
  const { data: matesData } = useMainCard2(selectedTeam);
  const { data: notificationsData, markAsRead } = useNotification(selectedTeam);

  const [selectedDate, setSelectedDate] = useState(
    new Date(new Date().setSeconds(0, 0)),
  );

  const { actionInfo, clearData } = useScheduleAction(
    selectedDate,
    recentScheduleData,
  );

  // TODO: 로딩 중 혹은 에러 발생 시 처리
  useHandlePop(() => {
    navigate(location.pathname, { replace: true });
  });

  useEffect(() => {
    let state = {};
    if (redirect) {
      navigate('/main', { replace: true });
      setTimeout(() => {
        if (teamId) {
          state = { teamId, senderId, isRegular: false };
        } else if (scheduleId) {
          state = { scheduleId, isRegular: true };
        } else if (scheduleDate) {
          state = { scheduleDate: new Date(scheduleDate) };
        }
        navigate(redirect, { state });
      }, 1500);
    }
  }, []);

  useEffect(() => {
    if (isFirstVisit && !waitingAppServerKey) {
      showModal(<OnboardingNotice setPushNoti={setPushNoti} />);
    } else if (!waitingAppServerKey) {
      setPushNoti();
    }
  }, [waitingAppServerKey]);

  useEffect(() => {
    clearData();
  }, [isScheduleOpen]);

  useEffect(() => {
    if (notificationsData) {
      setBanners(filterNotifications(notificationsData, selectedTeam));
    }
  }, [notificationsData]);

  useEffect(() => {
    if (recentScheduleData) {
      setTimeDiff(getScheduleTimeDiff(recentScheduleData));
    } else {
      setTimeDiff(null);
    }
  }, [recentScheduleData]);

  useEffect(() => {
    let filteredTeamList = [];

    if (teams.length > 0) {
      filteredTeamList = teams.filter((team) => !checkIsFinished(team.endDate));
      setFilteredTeams(filteredTeamList);
    }

    if (
      filteredTeamList.length > 0 &&
      (!selectedTeam ||
        filteredTeamList.find((team) => team.id === selectedTeam) === undefined)
    ) {
      selectTeam(filteredTeamList[0].id);
    }
  }, [teams]);

  const getOnMainButtonClick = () => {
    if (filteredTeams?.length === 0) {
      return () => navigate('/teamspace/make');
    }
    if (!recentScheduleData) {
      return () => toggleSchedule();
    }
    if (timeDiff <= 0) {
      return () =>
        navigate('/feedback/send', {
          state: {
            scheduleId: recentScheduleData.scheduleId,
            isRegular: true,
          },
        });
    }
    return () => toggleTodoAdd();
  };

  return (
    <motion.div
      className='relative flex size-full flex-col overflow-hidden'
      initial={{ opacity: 0 }}
      animate={{ opacity: 1 }}
      exit={{ opacity: 0 }}
      transition={{ duration: 0.14 }}
    >
      <div className='scrollbar-hidden size-full overflow-x-hidden overflow-y-auto'>
        <StickyWrapper className='px-5'>
          {filteredTeams && (
            <Accordion
              isMainPage={true}
              selectedTeamId={selectedTeam}
              teamList={filteredTeams}
              onTeamClick={selectTeam}
              isAllAlarmRead={
                notificationsData &&
                notificationsData.filter((noti) => noti.read === false)
                  .length === 0
              }
              onClickLastButton={() => navigate('/teamspace/make')}
            />
          )}
        </StickyWrapper>
        {banners?.length > 0 && (
          <motion.div
            initial={{ opacity: 0, y: -10 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{
              y: {
                type: 'spring',
                stiffness: 400,
                damping: 12,
              },
              duration: 0.23,
              ease: 'circOut',
            }}
          >
            <Slider {...sliderSettings} className='my-4 pb-2'>
              {banners.map((banner, index) => (
                <div className='px-[6px]' key={index}>
                  <Banner banner={banner} onClose={markAsRead} />
                </div>
              ))}
            </Slider>
          </motion.div>
        )}
        <div className='h-2' />
        {/* 로컬 스토리지 관련 문제 잡히면 다시 보기 */}
        {timeDiff !== undefined && (
          <motion.div
            initial={{ opacity: 0, y: 0 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.23, ease: 'circOut' }}
          >
            <MainCard
              userId={userId}
              isInTeam={filteredTeams.length > 0}
              recentSchedule={recentScheduleData}
              scheduleDifferece={timeDiff}
              onClickMainButton={getOnMainButtonClick()}
              onClickSubButton={() => toggleSchedule()}
              onClickChevronButton={() => navigate('/calendar')}
            />
          </motion.div>
        )}
        <div className='h-8' />
        {matesData && (
          <motion.div
            initial={{ opacity: 0, y: 0 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.23, ease: 'circOut' }}
          >
            <TeamMatesCard
              teamMates={matesData}
              onReceivedFeedbackClick={() =>
                navigate(
                  `/feedback/received?teamName=${filteredTeams.find((team) => team.id === selectedTeam).name}`,
                )
              }
              onClick={(mate) =>
                showModal(
                  <Modal
                    type={ModalType.PROFILE}
                    profileImage={
                      <div className='size-[62px]'>
                        <ProfileImage
                          iconName={`@animals/${mate.profileImage.image}`}
                          color={mate.profileImage.backgroundColor}
                        />
                      </div>
                    }
                    title={
                      mate.id === userId ?
                        `${mate.name}(나)`
                      : `${mate.name}님에게`
                    }
                    mainButton={
                      <MediumButton
                        text={
                          mate.id === userId ? '회고 작성하기' : '피드백 보내기'
                        }
                        onClick={() => {
                          mate.id === userId ?
                            navigate(`/feedback/self`)
                          : handleFreqFeedbackReq(navigate, {
                              teamId: selectedTeam,
                              senderId: mate.id,
                              senderName: mate.name,
                            });
                          hideModal();
                        }}
                        isOutlined={false}
                        disabled={false}
                      />
                    }
                    subButton={
                      mate.id === userId ?
                        null
                      : <MediumButton
                          text='피드백 요청하기'
                          onClick={() => {
                            navigate(
                              `/feedback/request?receiverId=${mate.id}&receiverName=${mate.name}`,
                            );

                            hideModal();
                          }}
                          isOutlined={true}
                          disabled={false}
                        />
                    }
                  />,
                )
              }
            />
          </motion.div>
        )}
        <div className='h-8' />
      </div>
      <ScheduleAction
        type={ScheduleActionType.ADD}
        isOpen={isScheduleOpen}
        onClose={() => toggleSchedule()}
        selectedDateFromParent={selectedDate}
        actionInfo={actionInfo}
        dateFixed={false}
        setParentDate={setSelectedDate}
      />
      {recentScheduleData && (
        <TodoAdd
          isOpen={isTodoAddOpen}
          onClose={() => toggleTodoAdd()}
          selectedSchedule={recentScheduleData}
        />
      )}
    </motion.div>
  );
}

const sliderSettings = {
  dots: true,
  arrows: false,
  infinite: false,
  speed: 500,
  slidesToShow: 1,
  slidesToScroll: 1,
  centerMode: true,
  centerPadding: '14px',
};
