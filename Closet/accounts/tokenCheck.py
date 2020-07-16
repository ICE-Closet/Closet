import jwt
import json

from .models import Account
from .my_settings import SECRET_KEY
from django.http import JsonResponse

# 함수에 @LoginConfirm 데코레이션을 붙여서 사용.
# 참고 : except jwt.DecodeError:https://wave1994.tistory.com/66?category=872868
# jwt 해석 : https://jwt.io/
class LoginConfirm:
    def __init__(self, original_function):
        self.original_function = original_function

    def __call__(self, request, *args, **kwargs):
        token = request.headers.get("Authorizations", None)
        try:
            if token:
                token_payload = jwt.decode(token, SECRET_KEY['secret'], SECRET_KEY['algorithm'])
                user = Account.objects.get(id=token_payload['user']) # 이부분
                request.user = user # ?
                print(user.id, user.username)
                return self.original_function(self, request, *args, **kwargs)

            return JsonResponse({'msg' : 'need login'}, status=401) # app과 맞춰보기
        
        except jwt.ExpiredSignatureError:
            return JsonResponse({'msg':'Expired token'}, status=401)
        
        except jwt.DecodeError:
            return JsonResponse({'msg':'invalid user'}, status=401)
        
        except Account.DoesNotExist:
            return JsonResponse({'msg':'invalid user'}, status=401)