def message(domain, uidb64, token):
    return f"아래 링크를 클릭하여 회원가입 인증을 완료해주세요. \n\n회원가입 링크 : http://localhost:8000/accounts/activate/{uidb64}/{token}"