from .models import Account, Clothes_category, Social_Login
from .my_settings import SECRET_KEY, EMAIL

import jwt
from django.http import JsonResponse

def social_login(platform, uid, email):
    platform, uid, email = platform, uid, email
    print('platform : ',  platform, 'email:', email, 'uid : ', uid)
    username = email.split('@')[0]

    my_social_user = Social_Login.objects.filter(uid=uid)

    # if myuser : # 같은 email주소가 있다면
    #     return JsonResponse({'code':1, 'msg':'duplicated email'}, status=201)
    if my_social_user: # 소셜로그인으로 로그인한적 있으면(db에 안넣어줘도돼)
        pass
    else: # 소셜로그인이 처음이면 -> uid 저장 + user info 저장
        social_form = Social_Login(platform=platform, uid=uid)
        social_form.save()
        social = Social_Login.objects.get(uid=uid)
        form = Account(email=email, username=username, is_active=True, social_id=social.id)
        form.save()

    myuser = Account.objects.get(email=email)
    token = jwt.encode({'user':myuser.id}, SECRET_KEY['secret'], SECRET_KEY['algorithm']).decode('UTF-8')
    print("token = ", token)
    result = {'token':token}#'raspberryPi':myuser.raspberrypiId
    return result
