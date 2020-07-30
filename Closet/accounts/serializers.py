from rest_framework import serializers
from .models import Account, Clothes_category, Social_Login
class AccountSerializer(serializers.ModelSerializer):
    class Meta:
        model = Account
        fields = ['email', 'password', 'username']

class SocialLoginSerializer(serializers.ModelSerializer):
    class Meta:
        model = Social_Login
        fields = ['paltform', 'uid']

class ClothesInfoSerializer(serializers.ModelSerializer):
    image = serializers.ImageField(use_url=True)

    class Meta:
        model = Clothes_category
        fields = ['image', 'color', 'top', 'bottom', 'outer']
