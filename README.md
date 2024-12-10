# Timely
### ***개인 & 그룹 스케줄 관리 및 지각 방지 Application***
<br><br>
## 01. 프로젝트 개요
개인 & 그룹 스케줄 한번에 관리     
개인 스케줄 시간 관리   
그룹모임 약속시간 전 위치 거짓말 방지    
  ex) "나 지금 가고있어~"(사실 아직 집이야) X   
<br><br>




## 02. 프로젝트 구성
### ***개발 기간***   
2024.10.23 ~ 2024.11.19 (4주)
<br><br>

### ***담당***   
기획: 이동수    
디자인: 이동수    
Android: 이동수   
BackEnd: 유종환   
<br><br>

### ***모듈 구성***
![image](https://github.com/user-attachments/assets/84a1a595-3903-4e5e-afd4-44a9203231a8)

<br><br>

### ***사용 기술***
| **Category** | **Technologies** |
| --- | --- |
| **Architecture** |  Multi Module,  Clean Architecture,  MVVM,  SAA  |
| **DI** | Dagger-Hilt |
| **Networking** |  Retrofit,  OkHttp,  Interceptor,  Gson  |
| **Database** | RoomDB |
| **Data Storage** |  Datastore,  Preferences  |
| **UI/UX** |  Glide,  Lottie,  Material 3,  VeiwPager2 + TabLayout  |
| **Navigation** |  Navigation Graph  |
| **Location & Maps** |  TMap API,  Kakao Map API,  FusedLocationProviderClient  |
| **Permissions** |  Runtime Permission,  TedPermission  |
| **Notifications** |  FCM,  AlarmManager  |
| **Background Processing** |  WorkManager,  Foreground Service,  Broadcast Receiver  |
| **UI State** |  StateFlow  |
| **Login & Social** |  Kakao Login,  Kakao Share  |
| **Concurrency** |  Thread,  Coroutine,  Flow  |
| **Data Binding** |  ViewBinding,  FlowBinding  |

<br><br>
## 03. Screen 및 상세 기능
### 개인 스케줄
<table>
  <tr><th>캘린더</th><th>개인 스케줄 추가</th><th>위치 권한</th></tr>
  <tr>
    <td><img src = "https://github.com/user-attachments/assets/1e17ed53-7451-4b3a-a43e-173fd9b85933" width="200px"/></td>
    <td><img src = "https://github.com/user-attachments/assets/37619f38-6586-4418-b320-4bdd9eebd245" width="200px"/></td>
    <td><img src = "https://github.com/user-attachments/assets/e26095f2-cdc5-4511-b1ce-db7cba1be60b" width="200px"/></td>
  </tr>
</table>

<table>
  <tr><th>위치 검색</th><th>알람(시간, 거리 표시)</th><th>알람 클릭시 TMap 연결</th></tr>
  <tr>
    <td><img src = "https://github.com/user-attachments/assets/c1716a42-33db-4697-acf7-32811557ad48" width="200px"/></td>
    <td><img src = "https://github.com/user-attachments/assets/e55b3140-9a94-4b66-9423-2317499218f0" width="200px"/></td>
    <td><img src = "https://github.com/user-attachments/assets/ac84982e-f33e-4b72-af5f-cb4f5b1c0d1d" width="200px"/></td>
  </tr>
</table>



| 캘린더| 개인 스케줄 추가 | 위치 권한 |
|---|---|---|
|![캘린더 영상 (online-video-cutter com) (1)](https://github.com/user-attachments/assets/3b7b38be-c4cf-4ae5-bff4-2ba0998726dd)|![일정추가 영상 (1)](https://github.com/user-attachments/assets/44063ccb-42bf-49b5-b83d-e2b357c031ad)|![위치 권한 영상 ](https://github.com/user-attachments/assets/9b1f4ec8-69f7-49d7-b08c-444d77682eb6)|
<br>
<br>
![일정추가 영상](https://github.com/user-attachments/assets/37619f38-6586-4418-b320-4bdd9eebd245)

| 위치검색 | 알람(시간, 거리 표시) | 알람 클릭시 TMap 연결|
|---|---|---|
|![위치검색 영상 -276](https://github.com/user-attachments/assets/41871bbc-2314-4d20-8f36-5a5f1b43294b)|![위치알람 스샷-276](https://github.com/user-attachments/assets/57401285-78dc-43dc-ac03-f22fa7c83529)|![TMap연결 시켜서 (online-video-cutter com) (1) (1)-276](https://github.com/user-attachments/assets/a52021f5-78a2-4c5f-9fc8-b9e02e92ca66)|
<br>
<br>

### 그룹 스케줄
| KaKao 로그인 | 내 그룹 리스트 | 그룹 생성 |
|---|---|---|
||![그룹 부분 영상-276](https://github.com/user-attachments/assets/266fbd25-47de-4d18-a69e-1b898d5003b8)||
<br>
<br>


| 그룹 페이지 | 그룹 초대 | 그룹 스케줄 생성 |
|---|---|---|
|![그룹페이지 영상 (리스트x)-276](https://github.com/user-attachments/assets/4dfd4395-ef18-4241-9bfe-b40042b05618)|||
<br>
<br>

| 스케줄 생성 알람 | 스케줄 1시간전 알람 | 위치 공유 시작 알람 |
|---|---|---|
||||
<br>
<br>

| 그룹 알람 | 그룹원 위치 확인 | 위치 확인 종료 |
|---|---|---|
||![위치 공유 영상 (2)](https://github.com/user-attachments/assets/afdf27d5-af6a-494a-bf49-4a96a1249f9c)|![지각시](https://github.com/user-attachments/assets/f1b8851b-2cee-4e4b-951e-6d600cd3041e)|
<br>
<br>


### 마이 프로필
| 지각률 확인 |
|---|
|![프로필 스샷 ](https://github.com/user-attachments/assets/d6e68576-953c-4317-8c7f-59debb809e25)|
<br>
<br>


