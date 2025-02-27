# 프로젝트 소개
피드백 주고 받기를 어려워하는 대학생들이 팀 프로젝트에서 건설적인 피드백을 작성하고 공유하는 과정을 도와주는 서비스입니다.

![image](https://github.com/user-attachments/assets/cca47e96-12f7-4c30-88ec-a1a07b2eea21)

## 주요 기능
저희 <피드한줌>의 주요 기능은 **피드백 요청하기/보내기, 일정 관리, 피드백 리포트** 이며, <br>
이 외에도 **회원가입/로그인, 보낸/받은 피드백 조회, 회고 작성/조회, 팀 스페이스 관리, 피드백 선호도 설정** 등이 가능합니다. <br>
아래는 주요 기능의 시연 영상입니다.

<table>
<tr>
<td><b>피드백 보내기</b><br><video src="https://github.com/user-attachments/assets/993102a0-bad2-40e1-9e85-b79e5b4b12d8" ></video>
<td><b>일정 추가</b><br><video src="https://github.com/user-attachments/assets/ec202737-1815-41c5-aba3-3688374c839e" ></video>
<td><b>피드백 리포트를 포함한 마이페이지 기능</b><br><video src="https://github.com/user-attachments/assets/42de714d-d610-44b3-a638-8e718f611981" ></video>
</table>

이외에 데모 화면들은 [여기](https://github.com/softeer5th/Team5-EQUUS-N/wiki/%EB%8D%B0%EB%AA%A8-%ED%99%94%EB%A9%B4)서 확인 가능합니다.

<br>


# 배포 URL
피드 한 줌: https://feedhanjum.com <br>
위 URL에 접속해 서비스 이용이 가능합니다.

<br>

# 팀원 소개

<table>
<tbody><tr>
    <td width="25%" align="center"><a href="https://github.com/GoGradually"><b>BE - 한준호</b></a></td>
    <td width="25%" align="center"><a href="https://github.com/vvsos1"><b>BE - 박명규</b></a></td>
    <td width="25%" align="center"><a href="https://github.com/lsj1137"><b>FE - 임세준</b></a></td>
    <td width="25%" align="center"><a href="https://github.com/hammsik"><b>FE - 백현식</b></a></td>
</tr>
<tr>
<td align="center"><a href="https://github.com/GoGradually"><img src="https://avatars.githubusercontent.com/u/62929862?v=4" width="180px;" alt=""/><br /></a><br />
<td align="center"><a href="https://github.com/vvsos1"><img src="https://avatars.githubusercontent.com/u/26290830?v=4" width="180px;" alt=""/><br /></a><br />
<td align="center"><a href="https://github.com/lsj1137"><img src="https://avatars.githubusercontent.com/u/57708892?v=44" width="180px;" alt=""/><br /></a><br />
<td align="center"><a href="https://github.com/hammsik"><img src="https://avatars.githubusercontent.com/u/116339092?v=4" width="180px;" alt=""/><br /></a><br />
</td>
</tr>
<tr>
    <td align="start">- 사용자 도메인<br>- 팀 도메인<br>- 일정 도메인<br>- AI 다듬기 </td>
    <td align="start">- 피드백 도메인<br>- 알림 도메인<br>- 구글 로그인<br>- CI/CD 구축</td>
    <td align="start">- 회원가입/로그인 페이지<br>- 일정 페이지<br>- 팀 스페이스 만들기 페이지<br>- 팀 스페이스 관리 페이지<br>- 받은 피드백 조회 페이지<br>- 보낸 피드백 조회 페이지<br>- 회고 조회 페이지<br>- 비밀번호 초기화 페이지<br>- PWA, 웹 푸시알림</td>
    <td align="start">- 메인 페이지<br>- 피드백 보내기 페이지<br>- 피드백 요청하기 페이지<br>- 마이페이지<br>- 프로필 수정 페이지<br>- 피드백 리포트 페이지<br>- API 모듈화 및 Mocking</td></tr>
</tbody>
</table>
<br/>

<table>
</table>

<br>

# 브랜치 전략

- GitHub Flow 전략 사용
    - main: main, release 브랜치
    - dev: 프론트/백엔드 공유용
    - feature: JIRA 의 백로그에 따른 기능 브랜치
        - 기능 + 테스트 함께 개발
        - 기능개발 완료 혹은 퇴근 전에 dev 로 pull request 올리기
            - 본인 제외 아무나 1명이 merge 하기

<br>

# 기획 디자인 링크

https://www.figma.com/design/76hKNmM5L0ba1xrDnxbEwd/Handoff_EQUUS-N-(Copy)?node-id=1-6&p=f

<br>

# 도메인 모델
![도메인 모델](./docs/domain_model.png)

# 서비스 아키텍처

## 백엔드
![backend_architecture.png](docs/backend_architecture.png)

<br>

## 프론트엔드
![img.png](docs/frontend_architecture.png)

<br>

# CI/CD 워크플로우
![img.png](docs/ci_cd_workflow.png)

<br>

# 기술 스택
## 공통

![github](https://img.shields.io/badge/github-181717?style=for-the-badge&logo=github&logoColor=white)
![github-actions](https://img.shields.io/badge/github%20actions-2088FF?style=for-the-badge&logo=github-actions&logoColor=white)
![swagger](https://img.shields.io/badge/swagger-85EA2D?style=for-the-badge&logo=swagger&logoColor=black)
![jira](https://img.shields.io/badge/jira-0052CC?style=for-the-badge&logo=jira&logoColor=white)
![Web Push](https://img.shields.io/badge/Web%20Push-000000?style=for-the-badge)

<br>

## 프론트엔드
![vite](https://img.shields.io/badge/vite-646CFF?style=for-the-badge&logo=vite&logoColor=yellow)
![react](https://img.shields.io/badge/react-61DAFB?style=for-the-badge&logo=react&logoColor=black)
![react-query](https://img.shields.io/badge/reactquery-FF4154?style=for-the-badge&logo=reactquery&logoColor=white)
![reactrouter](https://img.shields.io/badge/reactrouter-CA4245?style=for-the-badge&logo=reactrouter&logoColor=white)
![tailwindcss](https://img.shields.io/badge/tailwindcss-06B6D4?style=for-the-badge&logo=tailwindcss&logoColor=white)
![pwa](https://img.shields.io/badge/pwa-5A0FC8?style=for-the-badge&logo=pwa&logoColor=white)
![amazonwebservices](https://img.shields.io/badge/cloudfront-232F3E?style=for-the-badge&logo=amazonwebservices&logoColor=orange)
![amazons3](https://img.shields.io/badge/amazons3-569A31?style=for-the-badge&logo=amazons3&logoColor=white)
![motion](https://img.shields.io/badge/motion-0055FF?style=for-the-badge&logo=framer&logoColor=white)
![msw](https://img.shields.io/badge/mockserviceworker-FF6A33?style=for-the-badge&logo=mockserviceworker&logoColor=white)


<br>


## 백엔드
![Spring Web MVC](https://img.shields.io/badge/Spring%20Web%20MVC-%236DB33F?style=for-the-badge&logo=spring&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-%236DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![Spring AI](https://img.shields.io/badge/Spring%20AI-%236DB33F?style=for-the-badge&logo=spring&logoColor=white)
![Spring Mail](https://img.shields.io/badge/Spring%20Mail-%236DB33F?style=for-the-badge&logo=spring&logoColor=white)
![Querydsl](https://img.shields.io/badge/Querydsl-009688?style=for-the-badge)
![JPA](https://img.shields.io/badge/JPA-5C5C5C?style=for-the-badge&logo=hibernate&logoColor=white)
![Redis](https://img.shields.io/badge/Redis-%23DC382D?style=for-the-badge&logo=redis&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-%234479A1?style=for-the-badge&logo=mysql&logoColor=white)
![amazon EC2](https://img.shields.io/badge/EC2-%23FF9900?style=for-the-badge&logo=amazon-ec2&logoColor=white)
![AWS RDS](https://img.shields.io/badge/RDS-%23FF9900?style=for-the-badge&logo=amazon-rds&logoColor=white)
![AWS CloudWatch](https://img.shields.io/badge/CloudWatch-%23FF9900?style=for-the-badge&logo=amazon-cloudwatch&logoColor=white)
![JUnit5](https://img.shields.io/badge/JUnit5-25A162?style=for-the-badge&logo=junit5)
![Mockito](https://img.shields.io/badge/mockito-25A162?style=for-the-badge&logo=mocha)

<br>

# [📚 프로젝트 위키](https://github.com/softeer5th/Team5-EQUUS-N/wiki)
