# Timely
### ***개인 & 그룹 스케줄 관리 및 지각 방지 Application***
## 01. 프로젝트 개요
개인 & 그룹 스케줄 한번에 관리     
개인 스케줄 시간 관리   
그룹모임 약속시간 전 위치 거짓말 방지    
  ex) "나 지금 가고있어~"(사실 아직 집이야) X   

## 02. 프로젝트 구성
### ***개발 기간***   
2024.10.23 ~ 2024.11.19 (4주)

### ***담당***   
기획: 이동수    
디자인: 이동수    
Android: 이동수   
BackEnd: 유종환   
 
### ***모듈 구성***
![image](https://github.com/user-attachments/assets/84a1a595-3903-4e5e-afd4-44a9203231a8)

### ***사용 기술***
| **Category** | **Technologies** |
| --- | --- |
| **Architecture** | Multi Module, Clean Architecture, MVVM, SAA |
| **DI** | Dagger-Hilt |
| **Networking** | Retrofit, OkHttp, Interceptor, Gson|
| **Database** | RoomDB |
| **Data Storage** |  Datastore, Preferences |
| **UI/UX** | Glide, Lottie, Material 3, VeiwPager2 + TabLayout |
| **Navigation** | Navigation Graph |
| **Location & Maps** | TMap API, Kakao Map API, FusedLocationProviderClient |
| **Permissions** | Runtime Permission, TedPermission |
| **Notifications** | FCM, AlarmManager |
| **Background Processing** | WorkManager, Foreground Service, Broadcast Receiver |
| **UI State** |StateFlow|
| **Login & Social** | Kakao Login, Kakao Share |
| **Concurrency** | Thread, Coroutine, Flow |
| **Data Binding** | ViewBinding, FlowBinding |

## 03. Screen 및 상세 기능
### 개인 스케줄
| 캘린더| 개인 스케줄 추가 | 위치 권한 |
|---|---|---|
|![캘린더 영상](https://github.com/user-attachments/assets/0518bf5d-06f5-437b-9179-0b444b0aa15b)|![일정추가 영상](https://github.com/user-attachments/assets/2f11083f-cdd0-463a-93a2-dfa0c056e66d)|![위치 권한 영상 ](https://github.com/user-attachments/assets/4b54ed15-e7cb-4afb-a4b5-0fbd2cc02075)|


| 위치검색 | 알람 | TMap 연결|
|---|---|---|
|![위치 검색 스샷](https://github.com/user-attachments/assets/b03aa7db-26ab-4f81-954d-923490316161)|![위치알람 스샷](https://github.com/user-attachments/assets/32db328c-3783-4fe8-8217-7ab0c284cdd5)| 
 |

### 그룹 스케줄
| KaKao 로그인 | 내 그룹 리스트 | 그룹 생성 |
|---|---|---|
||![그룹 부분 영상](https://github.com/user-attachments/assets/b351ff3f-670c-46b8-b9b4-d5358585b6b2)||

| 그룹 페이지 | 그룹 초대 | 그룹 스케줄 생성 |
|---|---|---|
|![그룹페이지 영상 (리스트x)](https://github.com/user-attachments/assets/875895de-5d05-409a-8485-9b557934f93a)|||

| 그룹 알람 | 그룹원 위치 확인 | 위치 확인 종료 |
|---|---|---|
|||![지각시](https://github.com/user-attachments/assets/f1b8851b-2cee-4e4b-951e-6d600cd3041e)|


### 마이 프로필
| 지각률 확인 |
|---|
|![프로필 스샷 ](https://github.com/user-attachments/assets/d6e68576-953c-4317-8c7f-59debb809e25)|


