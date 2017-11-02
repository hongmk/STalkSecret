# STalkSecret
2017.09~2017.11 모바일교육

2017.09.15 진행내용
1) STalkSecret 프로젝트 생성
2) MainActivity 포함 16개 빈화면 생성
3) 4개 화면 레이아웃 초안 확인
4) activity_list.xlsx 파일에 액티비티 관련 요약
5) course_summary 디렉토리에 매주 강의내용 요약정리중

2017.09.15 추가진행
1) colors.xml , styles.xml 을 통한 스타일 지정
2) 로딩화면 레이아웃 변경 (Linear -> Relative) 및 스타일 지정
3) SigninActivity 레이아웃 조정 및 스타일 지정
4) Main->Signin Activity 전환구현

2017.09.22 진행
1) 마이페이지 초안작성
2) 닉네임변경, 비밀번호변경 초안작성
3) 로딩->로그인->홈->마이페이지->하위 화면 전환 구현
                   ->알림
4) 내글관리, 내댓글관리, 알림 페이지 리스트뷰 구현
5) 설정페이지 구현 및 마이페이지->설정 이동 구현

*이미지 간편편집은 GIMP사용가능


.......중략......

2017.11.02 진행상황

1. STalkSecret (Android)

1) 로그인화면
-레이아웃수정
-NodeJs연동 로그인 구현 (로그인 토큰 미수신시 로그인 실패로 간주)

2) 회원가입화면 더미 레이아웃 

3) 홈화면 
-WebView <-> Angular 화면 연동
-로그인 시 얻어온 부서코드로 게시글 작성 권한 부여(Floating button 숨김기능)
-글쓰기 화면연동
-메뉴에 마이페이지, 알림, 로그아웃 이동 연결

4) 글쓰기 화면 더미 레이아웃

5) 마이페이지
-모든 메뉴 더미페이지 구현

2. STalkSecretServer (Restful API Server)

*현재 NodeJS는 회원가입(각종검증포함), 로그인(토큰전달포함), 글쓰기, 게시판 전체글 조회, 특정게시글조회 가능

3. STalkAngular (FrontEnd Web)

-FrontEnd 쪽은 게시판글 전체 리스팅구현
-특정게시글 내용 조회 후 더미알림 구현
