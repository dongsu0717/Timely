# Timely
***개인 & 그룹 스케줄 관리 및 지각 방지 Application***

***개발 기간*** :  2024.10.23 ~ 2024.11.19 (4주)

***인원*** : Android 1명 + BackEnd 1명

***담당*** : 기획, 디자인, Android 개발 전체

***기술 스택***
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

***1 .  개인스케줄 - Calendar를 활용한 스케줄 관리***

- Calendar에서 달별로 내 스케줄 한눈에 확인 가능
- 스케줄별 다른 Color적용해 Category구분
- 스케줄 제목, 날짜, 장소, 알람여부 등 저장가능
- Login 없이 사용 가능
- `RoomDB`를 활용해 Data없이 사용 가능

![캐린더 스샷.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/e42eedae-e06f-408c-86b6-b54e340fe150/a6ff7d9c-6f51-46a6-890d-e8cf0ca81ff1/%EC%BA%90%EB%A6%B0%EB%8D%94_%EC%8A%A4%EC%83%B7.png)

