import { http, HttpResponse } from 'msw';

// export const handlers = [
//   http.get('/user', () => {
//     return HttpResponse.json({ name: 'John Maverick' })
//   }),
// ]
import {
  verifySignupResponse,
  signupResponse,
  updateScheduleResponse,
  schedules2,
  members2,
  feedbackReceivedResponse,
  feedbackSentResponse,
  teamResponse,
  retrospectResponse,
} from './mockData2';

const BASE_URL = 'https://api.feedhanjum.com';

export const handlers2 = [
  // 인증
  // http.get(`${BASE_URL}/api/auth/send-signup-verification-email`, () => {
  //   return HttpResponse.json(verifySignupResponse);
  // }),

  // 회원가입
  // http.post(`${BASE_URL}/api/auth/signup`, () => {
  //   return HttpResponse.json(signupResponse);
  // }),

  // 팀 일정 조회
  http.get(`${BASE_URL}/api/team/:teamId/schedules`, () => {
    return HttpResponse.json(schedules2);
  }),

  // 일정 수정
  http.put(`${BASE_URL}/api/team/:teamId/schedule/:scheduleId`, () => {
    return HttpResponse.json(updateScheduleResponse);
  }),

  // 팀 멤버 조회
  // http.get(`${BASE_URL}/api/team/:teamId/members`, () => {
  //   return HttpResponse.json(members2);
  // }),

  // 피드백 받은 내역 조회
  // http.get(`${BASE_URL}/api/feedbacks/receiver/:memberId`, () => {
  //   return HttpResponse.json(feedbackReceivedResponse);
  // }),

  // 피드백 보낸 내역 조회
  // http.get(`${BASE_URL}/api/feedbacks/sender/:memberId`, () => {
  //   return HttpResponse.json(feedbackSentResponse);
  // }),

  // 피드백 좋아요
  http.post(
    `${BASE_URL}/api/member/:memberId/feedbacks/:feedbackId/liked`,
    () => {
      return HttpResponse.json({ message: '좋아요를 눌렀습니다' });
    },
  ),

  // 피드백 좋아요 취소
  http.delete(
    `${BASE_URL}/api/member/:memberId/feedbacks/:feedbackId/liked`,
    () => {
      return HttpResponse.json({ message: '좋아요를 취소했습니다' });
    },
  ),

  // 리더 변경
  // http.post(`${BASE_URL}/api/team/:teamId/leader`, () => {
  //   return new HttpResponse({ status: 200 });
  // }),

  // 멤버 삭제
  // http.delete(`${BASE_URL}/api/team/:teamId/member/:memberId`, () => {
  //   return new HttpResponse({ status: 204 });
  // }),

  // 팀 수정
  // http.post(`${BASE_URL}/api/team/:teamId`, () => {
  //   return HttpResponse.json(teamResponse);
  // }),

  // 팀 삭제
  // http.delete(`${BASE_URL}/api/team/:teamId/leave`, () => {
  //   return HttpResponse.json(teamResponse);
  // }),
];
