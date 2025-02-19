// 0~24시까지 10분단위로 Date 객체 배열 생성
export const timeOptions = Array.from({ length: 144 }, (_, i) => {
  return (
    `${Math.floor(i / 6)}`.padStart(2, '0') +
    ':' +
    `${(i % 6) * 10}`.padStart(2, '0')
  );
});

/**
 * 시간 경과 계산 함수
 * @param {Date} date - 기준 날짜
 * @returns {string} - 시간 경과 문자열
 */
export function calTimePassed(date) {
  const now = new Date();
  const then = new Date(date);
  const diffMs = Math.abs(now - then);
  const diffSeconds = Math.floor(diffMs / 1000);
  const diffMinutes = Math.floor(diffSeconds / 60);
  const diffHours = Math.floor(diffMinutes / 60);
  const diffDays = Math.floor(diffHours / 24);
  if (diffSeconds < 60) {
    return `방금 전`;
  } else if (diffMinutes < 60) {
    return `${diffMinutes}분 전`; // 1시간 이내
  } else if (diffHours < 24) {
    return `${diffHours}시간 전`; // 1일 이내
  } else {
    return `${diffDays}일 전`; // 1일 이후
  }
}

/**
 * 날짜 정보 계산 함수
 * @param {Date} date - 기준 날짜
 * @returns {object} - 날짜 정보 객체
 */
export function getDateInfo(date) {
  // 요일 배열 (일요일부터 시작)
  const weekDays = ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'];

  // 선택한 날짜의 요일
  const dayOfDate = date.getDay();
  // 그 주의 목요일 날짜
  const dateOfThurs = new Date(date);
  dateOfThurs.setDate(dateOfThurs.getDate() + (4 - dayOfDate));
  // 이번주 목요일과 같은 년도와 월을 가진 달의 1일
  const firstDayThisMonth = new Date(
    dateOfThurs.getFullYear(),
    dateOfThurs.getMonth(),
    1,
  );
  // 1일의 요일 구하기
  const dayOfFirstDay = firstDayThisMonth.getDay();

  let diff = 0;
  // 날짜 차이 계산
  if (dayOfFirstDay < 5) {
    // 목요일 전이면 그만큼 더해서 계산
    diff = Math.ceil((dateOfThurs.getDate() + dayOfFirstDay) / 7);
  } else {
    // 목요일 이후면 그만큼 빼서 계산
    diff = Math.ceil((dateOfThurs.getDate() - (7 - dayOfFirstDay)) / 7);
  }

  return {
    weekDay: weekDays[dayOfDate], // 요일
    monthWeek: `${dateOfThurs.getMonth() + 1}월 ${diff}주차`, // 몇 월 몇 주차
    year: dateOfThurs.getFullYear(), // 년도
  };
}

/**
 * 최근 일요일 계산 함수
 * @param {Date} date - 기준 날짜
 * @returns {Date} - 최근 일요일 날짜
 */
export function getRecentSunday(date) {
  const sunday = new Date(new Date(date).setHours(0, 0, 0, 0));
  sunday.setDate(sunday.getDate() - sunday.getDay());
  return sunday;
}

export function changeDayName(dayName) {
  let day = '';
  if (dayName === 'Sun' || dayName === 0) {
    day = '일';
  } else if (dayName === 'Mon' || dayName === 1) {
    day = '월';
  } else if (dayName === 'Tue' || dayName === 2) {
    day = '화';
  } else if (dayName === 'Wed' || dayName === 3) {
    day = '수';
  } else if (dayName === 'Thu' || dayName === 4) {
    day = '목';
  } else if (dayName === 'Fri' || dayName === 5) {
    day = '금';
  } else if (dayName === 'Sat' || dayName === 6) {
    day = '토';
  }
  return day;
}

/**
 * 기준 날짜가 시작 날짜와 종료 날짜 사이에 있는지 확인하는 함수
 * @param {Date} stdDate - 기준 날짜
 * @param {Date} startDate - 시작 날짜
 * @param {Date} endDate - 종료 날짜
 * @returns {boolean} - 기준 날짜가 시작 날짜와 종료 날짜 사이에 있는지 여부
 */
export function timeInPeriod(stdDate, startDate, endDate) {
  const startTime = new Date(startDate).getTime();
  const endTime = new Date(endDate).getTime();
  const stdTime = new Date(stdDate).getTime();
  return startTime <= stdTime && endTime >= stdTime;
}

/**
 * 현 시간 기준 일정이 종료되었는지 확인하는 함수
 * @param {Date} date - 일정 종료 날짜
 * @returns {boolean} - 일정이 종료되었는지 여부
 */
export function checkIsFinished(date) {
  if (new Date(date).getTime().valueOf() < new Date()) {
    return true;
  } else {
    return false;
  }
}

export function timePickerToDate(date, time) {
  const [hour, minute] = time.split(':').map(Number);
  return new Date(
    date.getFullYear(),
    date.getMonth(),
    date.getDate(),
    hour,
    minute,
  );
}

/**
 * 년-월-일 문자열 반환
 * @param {Date} date - 날짜
 * @returns {string}
 */
export function toYMD(date) {
  const year = date.getFullYear();
  const month = (date.getMonth() + 1).toString().padStart(2, '0');
  const day = date.getDate().toString().padStart(2, '0');
  const formattedDate = `${year}-${month}-${day}`;
  return formattedDate;
}

/**
 *  date1의 날짜와 date2의 시간을 가진 Date객체 반환
 * @param {Date} date1
 * @param {Date} date2
 * @returns {Date}
 */
export function combineDateTime(date1, date2) {
  const year = date1.getFullYear();
  const month = date1.getMonth();
  const day = date1.getDate();

  const hours = date2.getHours();
  const minutes = date2.getMinutes();

  return new Date(year, month, day, hours, minutes);
}

/**
 * 표준시간 표기를 한국시간으로 변환해서 반환
 * @param {String | Date} date
 * @returns {Date}
 */
export function toKST(date) {
  let kst = new Date(date);
  kst.setTime(new Date(date).getTime() + 1000 * 60 * 60 * 9);
  return kst;
}

/**
 * D-day 계산하는 함수
 * @param {object} recentSchedule
 * @returns
 */
export function getScheduleTimeDiff(recentSchedule) {
  const todayTime = new Date();
  const startTime = new Date(recentSchedule.startTime);
  const endTime = new Date(recentSchedule.endTime);

  if (Math.abs(startTime - todayTime) < Math.abs(endTime - todayTime)) {
    // 미래 일정인 경우, today를 자정으로 설정하여 계산
    todayTime.setHours(0, 0, 0, 0);

    const diffDay = Math.floor((startTime - todayTime) / (1000 * 60 * 60 * 24));

    if (diffDay === 0) return 'DAY';
    return diffDay;
  } else {
    // 과거 일정인 경우
    return Math.ceil((endTime - todayTime) / (1000 * 60 * 60 * 24));
  }
}

/**
 * 현재 시간과 제일 가까운 미래의 10분단위의 시간 반환
 * @param {Date} currentTime - 현재 시간
 * @returns {date}
 */
export function getNearest10MinTime(currentTime) {
  const result = new Date(currentTime);

  result.setSeconds(0, 0);

  const minutes = result.getMinutes();
  const remainder = minutes % 10;

  const diff = remainder === 0 ? 10 : 10 - remainder;
  result.setMinutes(minutes + diff);
  return result;
}
