from django.shortcuts import render, redirect, HttpResponse
from django.contrib.auth.models import User
from .serializers import UserCreateSerializer
from rest_framework import viewsets, status, generics, permissions
from rest_framework.generics import CreateAPIView, GenericAPIView
from rest_framework.response import Response
from rest_framework.views import APIView
import traceback
from .models import User


class UserCreateAPIView(APIView):
    serializer_class = UserCreateSerializer
    def get(self, request, format=None):
        query_set = User.objects.all()
        return Response(request.data)

    def post(self, request, format=None):
        if(len(request.data['password']) < 4):
            body = {'message' : 'short field'}
            return Response(body, status=status.HTTP_400_BAD_REQUEST)
        serializer = self.get_serializer(data=request.data)
        if serializer.is_valid():
            user = serializer.save()
            return Response(serializer.data, status=status.HTTP_200_OK)
        return Response(serializer.error, status=status.HTTP_400_BAD_REQUEST)

        # return Response({
        #     "user":UserCreateSerializer(
        #         user, context=self.get_serializer_context()
        #     ).data,
        #     "token" : AuthToken.objects.create(user)[1],
        # })
        