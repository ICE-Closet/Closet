from django.db import models

class Social_Login(models.Model):
    platform = models.CharField(max_length=50) # kakao or google
    uid = models.CharField(max_length=50) # 고유 id

    class Meta:
        db_table = 'social_login'

# closet raspberry pi info
class RaspberryPi(models.Model):
    ip = models.CharField(max_length=50, unique=True)
    port = models.CharField(max_length=10)
    rasp_id = models.CharField(max_length=50)
    rasp_password = models.CharField(max_length=50)

    class Meta:
        db_table = 'raspberry_pi'

# app user info
class Account(models.Model):
    email = models.EmailField(max_length=100, unique=True) # email 겸 id
    password = models.CharField(max_length=200, null=True, blank=True) # social login시 null
    username = models.CharField(max_length=100)
    social = models.ForeignKey(Social_Login,on_delete=models.CASCADE, null=True, blank=True) # social_login db의 id
    is_active = models.BooleanField(default=False)
    #raspberry = models.ForeignKey(RaspberryPi, on_delete=models.CASCADE, default=True)
    
    def __str__(self):
        return self.username
    class Meta:
        db_table = 'account'

# user가 등록하는 옷들
class Clothes_category(models.Model):
    image = models.ImageField(upload_to="%Y/%m/%d", default=False, max_length=255)
    color = models.CharField(max_length=100, blank=True, null=True)
    top = models.CharField(max_length=50, blank=True, null=True)
    bottom = models.CharField(max_length=50, blank=True, null=True)
    outer = models.CharField(max_length=50, blank=True, null=True)
    class Meta:
        db_table = 'clothes_category'

# user + user의 clothes
class User_Closet(models.Model):
    user = models.ForeignKey(Account, on_delete=models.CASCADE)
    clothes = models.ForeignKey(Clothes_category, on_delete=models.CASCADE)
    class Meta:
        db_table = 'user_closet'