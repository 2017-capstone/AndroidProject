프로젝트 준수 규칙
===============

Environment
-----------
이 앱의 원활한 개발을 위해 다음의 환경을 준수할 것

> - 최소 지원 기종은 **Lolipop(5.0)**으로 정함
> - 리소스는 불가피한 경우를 제외하고 ic 파일을 사용할 것

Plugins
-------

이 앱의 원활한 개발을 위해 다음의 플러그인을 설치할 것

#### Android Drawable Importer
ic 리소스 파일을 사용하기 위해 [Android Drawable Importer](https://plugins.jetbrains.com/plugin/7658-android-drawable-importer)을 설치할 것.
설치 방법은 [링크](https://github.com/winterDroid/android-drawable-importer-intellij-plugin)를 참조

#### Gradle dependencies formatter
gradle 관리 및 피어 리뷰시 빠른 상황 판단을 위해 [Gradle dependency formatter](https://plugins.jetbrains.com/plugin/7937-gradle-dependencies-formatter)의 설치를 추천함. 설치 방법 및 이용방법은 [링크](https://github.com/platan/idea-gradle-dependencies-formatter)를 참조.

Works
-----

> - 할 일 목록을 [프로젝트](https://github.com/2017-capstone/AndroidProject/projects/1/edit)에서 공유할 것
> - **머지 기능을 사용 전 반드시 단톡에서 허락 받을 것**

Errors
------

> - 에러가 발생 시 [이슈](https://github.com/2017-capstone/AndroidProject/issues)에 문제상황을 개설할 것

Push
----

> - 푸시 전에 항상 풀을 할 것.
> - 특정 기능에 관한 작업을 시작할 때 브랜치를 개설하고 브랜치별로 [풀 요청](https://github.com/2017-capstone/AndroidProject/pulls)을 남길 것

호옥시나 해서 ... 작성해두는 브랜치+ 작성법
> - 예제: 탭에 메뉴를 다는 작업을 할거임 
> - "ADD_MENU" 작업임
> - Version Control에서 Log탭으로 간후,
> - 현재 origin/master라고 쓰인 부분에 마우스 오른쪽 클릭
> - New Branch 누르고, ADD_MENU
> - 다시 같은 곳을 마우스오른클릭 후, Branch 'ADD_MENU'가 있으면 클릭 후, Chekout 클릭
> - (1) 이후. 코딩작업
> - (2) 기능 단위로 작업완료시 Version Control에서 local changes 탭에서 Default클릭후, VCS(윗쪽화살표)클릭
> - 이후, (1)코딩작업...(2)(1)(2)... 반복
> - 이후, ADD_MENU작업이 끝났을때, (2)의 작업
> - 이후, Terminal선택
> - git checkout master (origin/master는 건들지 말것)
> - git merge ADD_MENU
> - 이후, git push 할것

> - 사랑해요 갓석진
