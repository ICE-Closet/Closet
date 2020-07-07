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
🔺 facebook 로그인 구현(rest-auth사용. facebook 개발자 홈페이지 가입. 처음 로그인 시도하면 db에 잘 저장되는데 redirect page를 어디로 설정해야하는지, 토큰을 어떻게 받는지 더 알아봐야함. 앱과 연동한 후 확인해봐야함.)<br>
✔️ 앱으로 signup + 실제 email로 인증메일 전송 + email 인증(is_active가 1로 변경) + login 확인.<br>
🔺 이메일 인증 링크 현재 192.168.0.1:88 로 전송된다.(text.py 에서 원래 {domain}으로 받아야 하는데 {domain}으로 받으면 로컬호스트로 돼서 사용자가 눌렀을 때 사이트가 뜨지 않는다.<br>
🔺 views.py class Activate 에서 REDIRECT_PAGE 를 지금 my_settings.py 에서 임의로 http://192.168.0.10:88/accounts/email-verify/  정해줬는데 이것도 바꿔야 한다.<br>
=> 아마 sites 부분을 고쳐야 할듯...? aws올려서 ip주소 나오면 그것도 등록해야한다
