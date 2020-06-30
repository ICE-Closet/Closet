from django.db import models
from django.contrib.auth.models import AbstractUser

class User(AbstractUser):
    username = models.CharField(max_length=150, null=False, unique=True)

    class Meta:
        db_table = 'User'