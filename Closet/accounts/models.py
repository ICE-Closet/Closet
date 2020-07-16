from django.db import models

# app user info
class Account(models.Model):
    email = models.EmailField(max_length=100, unique=True) # email 겸 id
    password = models.CharField(max_length = 200)
    username = models.CharField(max_length=100)
    is_active = models.BooleanField(default=False)
    
    def __str__(self):
        return self.username
    class Meta:
        db_table = 'account'

# user가 등록하는 옷들
class Clothes_category(models.Model):
    images = models.ImageField(upload_to="%Y/%m/%d", default=False)
    color = models.CharField(max_length=100)
    top = models.CharField(max_length=50)
    bottom = models.CharField(max_length=50)
    outer = models.CharField(max_length=50)
    class Meta:
        db_table = 'clothes_category'

# user + user의 clothes
class User_Closet(models.Model):
    user = models.ForeignKey(Account, on_delete=models.CASCADE)
    clothes = models.ForeignKey(Clothes_category, on_delete=models.CASCADE)
    class Meta:
        db_table = 'user_closet'