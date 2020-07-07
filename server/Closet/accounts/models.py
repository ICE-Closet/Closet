from django.db import models

class Account(models.Model):
    email = models.EmailField(max_length=100, unique=True) # email 겸 id
    password = models.CharField(max_length = 200)
    username = models.CharField(max_length=100)
    is_active = models.BooleanField(default=False)
    
    def __str__(self):
        return self.username
    class Meta:
        db_table = 'accounts'
    