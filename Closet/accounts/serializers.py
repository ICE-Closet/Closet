from rest_framework import serializers
from .models import Account, Clothes_category
class AccountSerializer(serializers.ModelSerializer):
    class Meta:
        model = Account
        fields = ['email', 'password', 'username']

class ClothesSerializer(serializers.ModelSerializer):
    image = serializers.ImageField(use_url=True)

    class Meta:
        model = Clothes_category
        fields = ('image')