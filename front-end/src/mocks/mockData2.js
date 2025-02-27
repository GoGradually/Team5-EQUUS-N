export const verifySignupResponse = {
  message: 'string',
};

export const signupResponse = {
  id: 9007199254740991,
  email: 'string',
  message: 'string',
};

export const loginResponse = {
  message: 'string',
  userId: 9007199254740991,
  email: 'string',
};

export const teams2 = [
  {
    id: 1234151,
    name: '소프티어 5조',
    startDate: '2025-01-02',
    endDate: '2025-02-28',
    feedbackType: 'ANONYMOUS',
    leader: {
      id: 52345513,
      name: '한준호',
      email: 'hanjunho@gmail.com',
      profileImage: {
        backgroundColor: '#62BFCA',
        image: 'panda',
      },
    },
  },
  {
    id: 4321,
    name: '협곡의 전사들',
    startDate: '2024-10-02',
    endDate: '2025-02-01',
    feedbackType: 'ANONYMOUS',
    leader: {
      id: 52345513,
      name: '백현식',
      email: 'baekhyeonsik@gmail.com',
      profileImage: {
        backgroundColor: '#62BFCA',
        image: 'penguin',
      },
    },
  },
  {
    id: 356456,
    name: '맘스터치 4조',
    startDate: '2024-12-02',
    endDate: '2025-05-01',
    feedbackType: 'ANONYMOUS',
    leader: {
      id: 52345513,
      name: '임세준',
      email: 'limsejun@gmail.com',
      profileImage: {
        backgroundColor: '#62BFCA',
        image: 'dolphin',
      },
    },
  },
];

export const schedules2 = [
  {
    scheduleId: 1,
    scheduleName: '스케줄 내용1',
    startTime: '2025-02-09T22:53:04.033Z',
    endTime: '2025-02-10T06:53:04.033Z',
    teamId: 1234151,
    teamName: '소프티어 5조',
    leaderId: 52345513,
    ownerId: 9007199254740991,
    scheduleMemberNestedDtoList: [
      {
        memberId: 1,
        memberName: '임세준',
        todoList: ['데스크 리서치 하기', '설문지 작성하기'],
      },
      {
        memberId: 2,
        memberName: '백현식',
        todoList: ['레퍼런스 리서치 하기', '설문지 배포하기'],
      },
      {
        memberId: 3,
        memberName: '한준호',
        todoList: ['데스크 리서치 하기', '설문지 작성하기'],
      },
      {
        memberId: 4,
        memberName: '박명규',
        todoList: ['프레젠테이션 연습하기', '설문지 작성하기'],
      },
    ],
  },
  {
    scheduleId: 5,
    scheduleName: '스케줄 내용1',
    startTime: '2025-02-10T06:53:04.033Z',
    endTime: '2025-02-11T06:53:04.033Z',
    teamId: 1234151,
    teamName: '소프티어 5조',
    leaderId: 52345513,
    ownerId: 9007199254740991,
    scheduleMemberNestedDtoList: [
      {
        memberId: 1,
        memberName: '임세준',
        todoList: ['데스크 리서치 하기', '설문지 작성하기'],
      },
      {
        memberId: 2,
        memberName: '백현식',
        todoList: ['레퍼런스 리서치 하기', '설문지 배포하기'],
      },
      {
        memberId: 3,
        memberName: '한준호',
        todoList: ['데스크 리서치 하기', '설문지 작성하기'],
      },
      {
        memberId: 4,
        memberName: '박명규',
        todoList: ['프레젠테이션 연습하기', '설문지 작성하기'],
      },
    ],
  },
  {
    scheduleId: 2,
    scheduleName: '스케줄 내용1',
    startTime: '2025-02-10T06:53:04.033Z',
    endTime: '2025-02-11T06:53:04.033Z',
    teamId: 1234151,
    teamName: '소프티어 5조',
    leaderId: 52345513,
    ownerId: 9007199254740991,
    scheduleMemberNestedDtoList: [
      {
        memberId: 1,
        memberName: '임세준',
        todoList: ['데스크 리서치 하기', '설문지 작성하기'],
      },
      {
        memberId: 2,
        memberName: '백현식',
        todoList: ['레퍼런스 리서치 하기', '설문지 배포하기'],
      },
      {
        memberId: 3,
        memberName: '한준호',
        todoList: ['데스크 리서치 하기', '설문지 작성하기'],
      },
      {
        memberId: 4,
        memberName: '박명규',
        todoList: ['프레젠테이션 연습하기', '설문지 작성하기'],
      },
    ],
  },
  {
    scheduleId: 3,
    scheduleName: '스케줄 내용1',
    startTime: '2025-02-12T06:53:04.033Z',
    endTime: '2025-02-13T06:53:04.033Z',
    teamId: 1234151,
    teamName: '소프티어 5조',
    leaderId: 52345513,
    ownerId: 9007199254740991,
    scheduleMemberNestedDtoList: [
      {
        memberId: 1,
        memberName: '임세준',
        todoList: ['데스크 리서치 하기', '설문지 작성하기'],
      },
      {
        memberId: 2,
        memberName: '백현식',
        todoList: ['레퍼런스 리서치 하기', '설문지 배포하기'],
      },
      {
        memberId: 3,
        memberName: '한준호',
        todoList: ['데스크 리서치 하기', '설문지 작성하기'],
      },
      {
        memberId: 4,
        memberName: '박명규',
        todoList: ['프레젠테이션 연습하기', '설문지 작성하기'],
      },
    ],
  },
  {
    scheduleId: 4,
    scheduleName: '스케줄 내용1',
    startTime: '2025-02-12T06:53:04.033Z',
    endTime: '2025-02-13T06:53:04.033Z',
    teamId: 4321,
    teamName: '협곡의 전사들',
    leaderId: 52345513,
    ownerId: 9007199254740991,
    scheduleMemberNestedDtoList: [
      {
        memberId: 1,
        memberName: '임세준',
        todoList: ['데스크 리서치 하기', '설문지 작성하기'],
      },
      {
        memberId: 2,
        memberName: '백현식',
        todoList: ['레퍼런스 리서치 하기', '설문지 배포하기'],
      },
      {
        memberId: 3,
        memberName: '한준호',
        todoList: ['데스크 리서치 하기', '설문지 작성하기'],
      },
      {
        memberId: 4,
        memberName: '박명규',
        todoList: ['프레젠테이션 연습하기', '설문지 작성하기'],
      },
    ],
  },
];

export const createScheduleResponse = {
  message: 'string',
};

export const updateScheduleResponse = {
  message: 'string',
};

export const members2 = [
  {
    id: 5145125,
    name: '임세준',
    email: 'limsejun@gmail.com',
    profileImage: {
      backgroundColor: '#62BFCA',
      image: 'penguin',
    },
  },
  {
    id: 5145126,
    name: '백현식',
    email: 'baekhyeonsik@gmail.com',
    profileImage: {
      backgroundColor: '#F6BF77',
      image: 'whale',
    },
  },
  {
    id: 52345513,
    name: '한준호',
    email: 'hanjunho@gmail.com',
    profileImage: {
      backgroundColor: '#7EABD9',
      image: 'panda',
    },
  },
  {
    id: 5145128,
    name: '박명규',
    email: 'parkmyeonggu@gmail.com',
    profileImage: {
      backgroundColor: '#F6D480',
      image: 'parrot',
    },
  },
];

export const teamResponse = [
  {
    teamResponse: teams2[0],
    members: members2,
  },
];

export const feedbackReceivedResponse = {
  page: 1073741824,
  hasNext: true,
  content: [
    {
      feedbackId: 134124,
      isAnonymous: true,
      sender: {
        name: '임세준',
        backgroundColor: '#F6D480',
        image: 'dog_face',
      },
      objectiveFeedbacks: [
        '명확하게 적기',
        '해결점 위주',
        '항상 긍정적인 태도',
        '직설적인 말투',
        '사용자 중심 디자인',
      ],
      subjectiveFeedback:
        '매번 핵심이 되는 포인트를 잘 잡아주시고 논리적으로 설득을 잘 하시는 것 같아요. ',
      teamName: '협곡의전사들',
      liked: true,
      createdAt: '2025-02-07T15:06:25.273Z',
    },
    {
      feedbackId: 41234,
      isAnonymous: false,
      sender: {
        name: '임세준',
        backgroundColor: '#F6D480',
        image: 'dog_face',
      },
      objectiveFeedbacks: [
        '명확하게 적기',
        '항상 긍정적인 태도',
        '직설적인 말투',
        '사용자 중심 디자인',
      ],
      subjectiveFeedback:
        '매번 핵심이 되는 포인트를 잘 잡아주시고 논리적으로 설득을 잘 하시는 것 같아요. ',
      teamName: '협곡의전사들',
      liked: true,
      createdAt: '2025-02-07T15:06:25.273Z',
    },
    {
      feedbackId: 2452435,
      isAnonymous: false,
      sender: {
        name: '임세준',
        backgroundColor: '#F6D480',
        image: 'dog_face',
      },
      objectiveFeedbacks: ['명확하게 적기', '친절한 설명'],
      subjectiveFeedback:
        '매번 핵심이 되는 포인트를 잘 잡아주시고 논리적으로 설득을 잘 하시는 것 같아요. ',
      teamName: '협곡의전사들',
      liked: true,
      createdAt: '2025-02-07T15:06:25.273Z',
    },
    {
      feedbackId: 5461234,
      isAnonymous: true,
      sender: {
        name: '임세준',
        backgroundColor: '#F6D480',
        image: 'dog_face',
      },
      objectiveFeedbacks: ['명확하게 적기'],
      subjectiveFeedback:
        '매번 핵심이 되는 포인트를 잘 잡아주시고 논리적으로 설득을 잘 하시는 것 같아요. ',
      teamName: '협곡의전사들',
      liked: true,
      createdAt: '2025-02-07T15:06:25.273Z',
    },
  ],
};

export const feedbackSentResponse = {
  page: 1073741824,
  hasNext: true,
  content: [
    {
      feedbackId: 1341234,
      isAnonymous: false,
      receiver: {
        name: '백현식',
        backgroundColor: '#F6D480',
        image: 'lion',
      },
      objectiveFeedbacks: ['명확하게 적기', '친절한 설명'],
      subjectiveFeedback:
        '매번 핵심이 되는 포인트를 잘 잡아주시고 논리적으로 설득을 잘 하시는 것 같아요. ',
      teamName: '협곡의전사들',
      liked: true,
      createdAt: '2025-02-07T15:07:05.819Z',
    },
    {
      feedbackId: 256243,
      isAnonymous: true,
      receiver: {
        name: '백현식',
        backgroundColor: '#F6D480',
        image: 'lion',
      },
      objectiveFeedbacks: ['명확하게 적기', '친절한 설명'],
      subjectiveFeedback:
        '매번 핵심이 되는 포인트를 잘 잡아주시고 논리적으로 설득을 잘 하시는 것 같아요. ',
      teamName: '협곡의전사들',
      liked: true,
      createdAt: '2025-02-07T15:07:05.819Z',
    },
    {
      feedbackId: 12341324,
      isAnonymous: true,
      receiver: {
        name: '백현식',
        backgroundColor: '#F6D480',
        image: 'lion',
      },
      objectiveFeedbacks: ['명확하게 적기', '친절한 설명'],
      subjectiveFeedback:
        '매번 핵심이 되는 포인트를 잘 잡아주시고 논리적으로 설득을 잘 하시는 것 같아요. ',
      teamName: '협곡의전사들',
      liked: true,
      createdAt: '2025-02-07T15:07:05.819Z',
    },
    {
      feedbackId: 23452,
      isAnonymous: false,
      receiver: {
        name: '백현식',
        backgroundColor: '#F6D480',
        image: 'lion',
      },
      objectiveFeedbacks: ['명확하게 적기', '친절한 설명'],
      subjectiveFeedback:
        '매번 핵심이 되는 포인트를 잘 잡아주시고 논리적으로 설득을 잘 하시는 것 같아요. ',
      teamName: '협곡의전사들',
      liked: true,
      createdAt: '2025-02-07T15:07:05.819Z',
    },
  ],
};

export const retrospectResponse = {
  page: 1073741824,
  hasNext: true,
  content: [
    {
      title: '1주차 회의 회고',
      teamName: '소프티어 5조',
      content:
        '팀원들과 친해지는 기간이었다. 다함께 힘을 모아서 잘 마무리 되었으면 좋겠다.',
      createdAt: '2025-02-09T11:15:45.827Z',
    },
    {
      title: '2주차 회의 회고',
      teamName: '소프티어 5조',
      content:
        '이번 프로젝트를 통해 팀원들과의 소통이 얼마나 중요한지 다시 한번 느낌. 특히 중간 점검 회의에서 나온 피드백이 큰 도움이 되었고, 최종 결과물의 완성도를 높이는 데 기여했다. 다음에는 일정 관리를 더욱 철저히 하고, 사전 준비를 강화해야겠다고 느껴졌음.',
      createdAt: '2025-02-09T11:15:45.827Z',
    },
    {
      title: '3주차 회의 회고',
      teamName: '소프티어 5조',
      content:
        '이번 프로젝트를 통해 팀원들과의 소통이 얼마나 중요한지 다시 한번 느낌. 특히 중간 점검 회의에서 나온 피드백이 큰 도움이 되었고, 최종 결과물의 완성도를 높이는 데 기여했다. 다음에는 일정 관리를 더욱 철저히 하고, 사전 준비를 강화해야겠다고 느껴졌음.',
      createdAt: '2025-02-09T11:15:45.827Z',
    },
    {
      title: '4주차 회의 회고',
      teamName: '소프티어 5조',
      content:
        '나의 의견을 제대로 피력하지 못해 좀 아쉬웠다. 다음부턴 명확하게 전달할 수 있도록 노력해야겠다.',
      createdAt: '2025-02-09T11:15:45.827Z',
    },
  ],
};
