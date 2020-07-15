## Closet - Server Part

##### 6/29 (월)
✔️ django 환경 설정(pip, venv ...) <br>
✔️ aws putty 접속해보기 <br>
✔️ git organization 생성 -> 각자 branch 생성 <br>

##### 6/30 (화)
✔️django mysql 연결 <br>
✔️signup 코드짜기 <br>

##### 7/1 (수) ~ 7/3 (금)
✔️signup, login 을 코드를 위한 drf 공부 <br>
(rest-auth registration을 사용하여 코드를 다 짰는데 원하는대로 안돼서 조금 헤맸다)<br>

##### 7/6 (월)
✔️ signup, login 코드 완성 (그냥 일반 model 정의하여 사용. rest-auth 코드 사용안함) <br>
✔️ 회원가입 후 이메일 인증 코드 작성(이메일이 실제 보내지지 않음. shell에서는 보낼 이메일내용과 1이 뜨긴함. 원래 테스트로 보낼때도 이메일이 실제로 보내졌는데 지금은 shell에서만 뜸.)<br>
-> 해결 : settings.py 에서 EMAIL_BACKEND = 'django.core.mail.backends.smtp.EmailBackend' 의 smtp 가 consol로 되어있었다.

##### 7/7 (화)
✔️ 앱으로 signup + 실제 email로 인증메일 전송 + email 인증(is_active가 1로 변경) + login 확인.<br>
~~❗️ facebook 로그인 구현(rest-auth사용. facebook 개발자 홈페이지 가입. 처음 로그인 시도하면 db에 잘 저장되는데 redirect page를 어디로 설정해야하는지, 토큰을 어떻게 받는지 더 알아봐야함. 앱과 연동한 후 확인해봐야함.) -> 알아보니 app 자체에서만 firebase 사용해서 소셜로그인 구현하는거였다..!~~<br>
🔺 이메일 인증 링크 설정 다시.(text.py 에서 {domain}으로 받는데 domain 주소 확인해봐야함!).<br>
🔺 views.py class Activate 에서 REDIRECT_PAGE 를 지금 my_settings.py 에서 임의로 http://192.168.0.10:88/accounts/email-verify/  정해줬는데 이것도 바꿔야 한다.<br>
=> 아마 sites 부분을 고쳐야 할듯...? aws올려서 ip주소 나오면 그것도 등록해야한다

##### 7/8 (수) ~ 7/9 (목)
✔️ aws 설정(생고생,,,ㅠㅠㅠ zsh설치한걸 지우는데 이상한걸 지워버려서 aws ssh접속을 못해서 다시 인스턴스 파고 무슨 기본설정도 지워버려서 다시 만들고 또 다시 팠다..)

##### 7/10 (금)
🔺 nginx 설정<br>
1. .config 폴더에 settings 파일들 추가<br>
2. aws ubuntu에 python 및 pyenv 설정 <br>
3. srv 폴더 생성, git clone(scp가 작동이 안되었다.. 왜지?) -> 지금 상위폴더 두개있는데 그냥 srv폴더 바로밑에 project 폴더 두기!! <br>
특정branch만 clone하는 법 : git clone -b {branch_name} --single-branch {저장소 URL} <br>
ex) git clone -b javajigi --single-branch https://github.com/javajigi/java-racingcar <br>
참고사이트 : https://lhy.kr/ec2-ubuntu-deploy <br>

##### 7/12 (일)
토요일은 아파서 스킵ㅜ
ssh 접속이 집에서는 되지 않는다(검색해보니 sk브로드밴드 와이파이로는 접속이 안된다더라..?)<br>
✔️ login시 발급되는 token인증 데코레이터 생성(client가 post보낼땐 header에 꼭 token을 포함하여 보내줘야함!) -> 앱과 연동해보기 <br>
json web token debugger : https://jwt.io/ <br>
🔺 앱에서 사진찍으면 db에 저장되도록 <br>
월요일에 할거 : nginx,aws 설정 마저하기, aws고정ip주소로 email 주소 바꾸기

##### 7/14 (화)
✔️ aws ec2 install (requirements.txt 를 install 하려고했는데 잘안되어서 필요한거 명령어 입력해서 설치했다.)<br>
✔️ aws ec2 mysql 설치 (mysql ver : 5.7.30) -> aws rds 사용하면 요금나올수도있어서 ubuntu자체에 install 했다. <br>
✔️ aws 에 django project올리고 runserver해보기 (인바운드에 TCP, port 8080 추가, settings.py ALLOWED_HOST도 aws 주소 추가) <br>
<참고사이트> <br>
https://dejavuqa.tistory.com/317 (ubuntu mysql 설치, 설정) <br>
https://nachwon.github.io/django-deploy-1-aws/ (aws에서 django 배포. 그냥 runserver하는것) <br>
✔️ uWSGI 설정 <br>
내일할것: nginx 성공시키기 <br>
https://rainsound-k.github.io/deploy/2018/05/02/instance-setting-and-django-deploy-part2.html 사이트 4.2부분부터!<br>

##### 7/15 (수)
🔺 uWSGI 설정, nginx 설정 + 배포 <br>
설정 완료후 activate 는 뜨는데, Closet 이 no module error 뜬다. 찾아보니 pip 설치 경로문제일수도 있다는데 한번더 찾아봐야겠다. <br>
venv에서 기본 uwsgi 돌리면 잘돌아감. <br>
설정들이 다 건드리면 안되는 폴더에 들어있어서 인스턴스 새로 갈아 엎음. python3.7 설정. python3 명령어로 해야함!<br>
다 갈아엎고 clone한 후에(clone하고 폴더 경로 수정. git 올릴때 유의할것) pip install 하고, mysql연결. <br>
https://yuddomack.tistory.com/entry/%EC%B2%98%EC%9D%8C%EB%B6%80%ED%84%B0-%EC%8B%9C%EC%9E%91%ED%95%98%EB%8A%94-EC2-nginx%EC%99%80-uwsgi%EB%A1%9C-django-%EC%84%9C%EB%B9%84%EC%8A%A4%ED%95%98%EA%B8%B0 관련설정, 설정파일 경로는 이 사이트 참고했음.
