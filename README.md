# Timely
### ***개인 & 그룹 스케줄 관리 및 지각 방지 Application***
### 01. 프로젝트 개요
개인 & 그룹 스케줄 한번에 관리     
개인 스케줄 시간 관리   
그룹모임 약속시간 전 위치 거짓말 방지    
  ex) "나 지금 가고있어~"(사실 아직 집이야) X   

### 02. 프로젝트 구성
***개발 기간***   
2024.10.23 ~ 2024.11.19 (4주)

***담당***   
기획: 이동수    
디자인: 이동수    
Android: 이동수   
BackEnd: 유종환   
 
***모듈 구성***
![image](https://github.com/user-attachments/assets/84a1a595-3903-4e5e-afd4-44a9203231a8)

***사용 기술***
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

### 03. Screen 및 상세 기능
| 캘린더| 개인 스케줄 추가 | 위치 권한 |
|---|---|---|
|![캐린더 스샷](https://github.com/user-attachments/assets/8fe63960-102d-41e4-aaec-8ebfc796c280)| ![캐린더 스샷](https://github.com/user-attachments/assets/f6a8d9c8-2c9c-4c54-82a1-eff482e8423e) | ![캐린더 스샷](https://github.com/user-attachments/assets/0b0577a2-3714-4021-93ac-7b15d54bf4e6) |
