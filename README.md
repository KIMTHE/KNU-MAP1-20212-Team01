# KNU-MobileAppProgramming1-20212-Team1
- 경북대학교 모바일앱프로그래밍1 1팀입니다.
- 진행기간: 2021.10.10~2021.12.10

```
             TEAM : JONGSIP
컴퓨터학부 2017114915 김민수
컴퓨터학부 2017114846 김두영
컴퓨터학부 2017111547 박지민
컴퓨터학부 2017113223 이승우
```
<br/>

## Title  << 간식 뭐먹지? >>

<img src="https://github.com/KIMTHE/KNU-MAP1-20212-Team01/blob/main/android/app/src/main/ic_logo-playstore.png?raw=true" width="20%" height="15%" alt="TitleLogo"></img>

###### 노점관리 및 위치 서비스 어플리케이션

---------------------------------------------
## [목차]
- [💡개발배경](#개발배경)
- [📚Task 관리](#Task-관리)
- [💻주요 기능](#주요-기능)
- [👩‍💻기술스택](#기술스택)
- 📽[시연영상](#시연영상)
--------------------------------------------

## 💡개발배경

- [최종발표자료 참고](https://github.com/KIMTHE/KNU-MAP1-20212-Team01/blob/main/document/Project_final_1%EC%A1%B0.pptx)
- 기존 관련 앱을 보완하여 안드로이드 앱을 개발해보기로 했다. 다음 내용들은 보완된 점이다.
- 가게주인이 직접 노점을 등록함으로써, 신뢰성 있는 메뉴정보를 제공한다.
- 메뉴에 대한 가격,사진 등의 정보를 확인할 수 있다.
- 실시간으로 노점의 영업유무를 확인할 수 있다.

<br/>

## 📚Task 관리

###### 관련 링크
> - [git convention](https://github.com/KIMTHE/KNU-MAP1-20212-Team01/wiki/git-convention)
> - [회의록](https://github.com/KIMTHE/KNU-MAP1-20212-Team01/tree/main/document/%ED%9A%8C%EC%9D%98%EB%A1%9D)
> - [WBS(일정)](https://docs.google.com/spreadsheets/d/1NIOKkvimLdRiX4SEPnec7ZIy3rl9CCzilnVnqP7cev4/edit#gid=1115838130)
<br/>

## 💻주요 기능
- [기술문서 참고](https://github.com/KIMTHE/KNU-MAP1-20212-Team01/blob/main/document/Technical%20Documents_1%EC%A1%B0.hwp)
- 해당 기능들은 주로 firebase을 활용하였다. firebase 구조 및 자세한 기술내용은 위의 기술문서를 참고하길바란다.

#### 1️⃣회원가입 및 로그인을 할 수 있다.
  * 고객 또는 판매자 계정으로 회원가입 및 로그인을 할 수 있다, 고객과 판매자 계정은 화면구성이 다르다.

|로그인|회원가입|
|:-----:|:-----:|
|<img height=400 src="https://user-images.githubusercontent.com/29460783/149614098-470a9931-d1ea-45ff-84a2-52e36e34b54f.png">|<img height=400 src="https://user-images.githubusercontent.com/29460783/149614116-a0136619-ac61-4b4a-9dfb-5a3cc684ad37.png">|
</br>


#### 2️⃣지도에서 근처의 노점정보를 알 수 있다. 
   * 구글 map api를 이용하여 맵뷰를 구성하였다. 노점정보는 firebase에서 불러온다.
   * 노점 마커를 클릭하면 해당 노점의 판매되고있는 메뉴를 볼 수 있다.

|지도|메뉴|
|:-----:|:-----:|
|<img height=400 src="https://user-images.githubusercontent.com/29460783/149614147-d6548b19-0f20-4683-8e3b-f9593dff720f.png">|<img height=400 src="https://user-images.githubusercontent.com/29460783/149614150-6e3ba744-dfa7-47c7-8d62-1d043d4d91a8.png">|
</br>


#### 3️⃣현재 운영중인 노점의 메뉴를 검색할 수 있다.
   * firebase에서 검색한 메뉴를 불러오고, 클릭하면 지도에서 해당 노점위치로 이동한다.

|검색실패|검색성공|
|:-----:|:-----:|
|<img height=400 src="https://user-images.githubusercontent.com/29460783/149614197-79faf4bf-40a8-44fe-8b2b-2f3603a77e2d.png">|<img height=400 src="https://user-images.githubusercontent.com/29460783/149614198-f26885ab-5b2f-4dcc-8e89-a104844e5087.png">|
</br>


#### 4️⃣고객 계정은 노점을 북마크에 등록 할 수 있다.
   * 북마크한 노점정보를 firebase에서 불러오며, 각 요소를 클릭하면 해당 노점의 현재 판매되고있는 메뉴화면으로 이동한다.

|즐겨찾기|
|:-----:|
|<img height=400 src="https://user-images.githubusercontent.com/29460783/149614208-b8922d77-043c-48de-a455-48a4ad5d1eee.png">|
<br/>


#### 5️⃣판매자 계정은 노점의 정보 및 메뉴를 관리할 수 있다.
   * firebase에서 해당 계정의 노점정보를 불러온다. 내용을 수정하거나 메뉴를 추가할 수 있다.

|노점정보|메뉴추가|
|:-----:|:-----:|
|<img height=400 src="https://user-images.githubusercontent.com/29460783/149614228-d7803a0b-198b-4aa1-a963-7dd7a7253e68.png">|<img height=400 src="https://user-images.githubusercontent.com/29460783/149614230-3e9d0610-7339-4c72-9e90-ff0ae31dade2.png">|
<br/>



#### 6️⃣판매자 계정은 현재위치에 노점을 영업시작 및 종료할 수 있다.
   * 토글 버튼으로 구현하였으며, 영업시작 및 종료시 firebase에 업데이트된다.

|설정|
|:-----:|
|<img height=400 src="https://user-images.githubusercontent.com/29460783/149614247-0b86f750-117c-487f-af01-0036d0a98e33.png">|
<br/>


## 👩‍💻기술스택

<img src="https://img.shields.io/badge/kotlin-7F52FF?style=flat-square&logo=kotlin&logoColor=white"/></a>
<img src="https://img.shields.io/badge/Android-3DDC84?style=flat-square&logo=Android&logoColor=white"/></a>
</br>
<img src="https://user-images.githubusercontent.com/29460783/149613823-501bb597-b0bc-476f-b2e6-0167e05d1d35.png" width="40%" height="30%" alt="TitleLogo"></img>

<br/>

## 📽시연영상

[![Video Label](http://img.youtube.com/vi/lmAzREZpPi4/0.jpg)](https://www.youtube.com/watch?v=lmAzREZpPi4&ab_channel=%EA%B9%80%EB%91%90%EC%98%81)
