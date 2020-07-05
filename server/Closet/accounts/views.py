import json
import bcrypt
import jwt
from .models import Account
from .serializers import AccountSerializer
from .my_settings import SECRET_KEY
from django.views import View
from django.http import HttpResponse, JsonResponse
from django.views.decorators.csrf import csrf_exempt
from django.utils.decorators import method_decorator

@csrf_exempt
def signup(request, format=None):
    if request.method == "GET":
        queryset = Account.objects.all()
        serializer = AccountSerializer(queryset, many=True)
        return JsonResponse(serializer.data, safe=False)

    if request.method == "POST":
        email = request.POST.get("email", "")
        pw = request.POST.get("password", "")
        password = bcrypt.hashpw(pw.encode("UTF-8"), bcrypt.gensalt()).decode("UTF-8")
        username = request.POST.get("username", "")
        print("email = " + email+" username = " + username)
        myuser = Account.objects.filter(email=email)

        if myuser:
            print("duplicated email")
            return JsonResponse({'code':400, 'msg':'duplicated email'}, status=201)
        else : 
            form = Account(email=email, password=password, username=username, is_active=False)
            form.save()
            print("signup success")
            return JsonResponse({'code':201, 'msg':'signup success'}, status=201)