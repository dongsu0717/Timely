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
### 개인 스케줄 관리
<table>
  <tr><th>캘린더</th><th>개인 스케줄 추가</th><th>위치 권한</th></tr>
  <tr>
    <td><img src = "https://github.com/user-attachments/assets/1e17ed53-7451-4b3a-a43e-173fd9b85933" width=내</th></tr>
  <tr>
    <td><img src = "https://github.com/user-attachments/assets/c1716a42-33db-4697-acf7-32811557ad48" width="200px"/></td>
    <td><img src = "https://github.com/user-attachments/assets/ac84982e-f33e-4b72-af5f-cb4f5b1c0d1d" width="200px"/></td>
  </tr>
</table>
<br>

<table>
  <tr><th>알람 - 장소X(데이터X)</th></tr>
  <tr>
    <td><img src = "https://github.com/user-attachments/assets/5b2e8690-b668-4330-a22d-46266e7e7ca2" width="400px"/></td>
  </tr>
  <tr><th>알람 - 장소O(시간, 거리 표시)</th></tr>
  <tr>
    <td><img src = "https://github.com/user-attachments/assets/e55b3140-9a94-4b66-9423-2317499218f0" width="400px"/></td>
  </tr>
</table>
<br>

### 그룹 스케줄 관리
<table>
  <tr><th>KaKao 로그인</th><th>그룹 생성</th><th>내 그룹 리스트</th></tr>
  <tr>
    <td><img src = "https://github.com/user-attachments/assets/19d93e0f-8cb0-4278-bc57-4ce10c81131c" width="200px"/></td>
    <td><img src = "https://github.com/user-attachments/assets/3463b384-c367-41aa-b9ee-777146196c81" width="200px"/></td>
    <td><img src = "https://github.com/user-attachments/assets/a9089007-8d20-4d73-babe-a545e6bbdf98" width="200px"/></td>
  </tr>
</table>
<br>

<table>
  <tr><th>그룹 페이지</th><th>그룹 초대</th><th>그룹 스케줄 생성</th></tr>
  <tr>
    <td><img src = "https://github.com/user-attachments/assets/4f8e1b13-b7cc-45ab-b45c-e3a7ae465ff6" width="200px"/></td>
    <td><img src = "https://github.com/user-attachments/assets/449d69dc-694d-4984-8a92-10c8f2b5f3e7" width="200px"/></td>
    <td><img src = "https://github.com/user-attachments/assets/f38b0ae7-3ea9-454b-8151-65e28a4fc071" width="200px"/></td>
  </tr>
</table>
<br>

<table>
  <tr><th>스케줄 생성 알람</th><tr>
  <tr>
    <td><img src = "https://github.com/user-attachments/assets/7451f52d-f2af-4457-a035-df04d0669a48" width="400px"/></td>
  </tr>
  <tr><th>모임 1시간전 알람</th><tr>
  <tr>
    <td><img src = "https://github.com/user-attachments/assets/793eb24a-b737-432c-b84b-9c57d41db07c" width="400px"/></td>
  </tr>
  <tr><th>위치 공유 시작 알람</th><tr>
  <tr>
    <td><img src = "https://github.com/user-attachments/assets/9ac328de-7be1-4e0c-99d9-b6b77547f040" width="400px"/></td>
  </tr>
</table>
<br>

<table>
  <tr><th>그룹원 위치, 상태 확인</th><th>지각 체크후 지도 종료</th></tr>
  <tr>
    <td><img src = "https://github.com/user-attachments/assets/c75d115b-caa5-4f05-a54d-4d921355d342" width="200px"/></td>
    <td><img src = "https://github.com/user-attachments/assets/961afc8d-9cc1-48df-a1f0-dd062d3a2ccf" width="200px"/></td>
  </tr>
</table>
<br>

### 마이 페이지
<table>
  <tr><th>지각률 확인</th></tr>
  <tr>
    <td><img src = "https://github.com/user-attachments/assets/abaadb1f-d0ae-489c-af9e-cd93c1597abc" width="200px"/></td>
  </tr>
</table>
<br>


