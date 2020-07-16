### Description of codes 😀😀😀😀
1. SmallerVGGNet : 사용할 Keras 신경망 아키텍처이다.
2. How to quickly build a deep learning image? Bing Image Search API(Microsoft)를 사용하여 이미지 데이터 세트를 쉽게 구축할 수 있다.<br>
    2-1. 아래 링크타고 들어가서  Bing Image Search API의 GET API KEY 수행하기 <br>
     👉[API설정](https://azure.microsoft.com/en-us/try/cognitive-services/?api=bing-image-search-api) <br>
    2-2. 아래 모듈 설치하기하고 코드 실행!( 나머지는 API 사용 방법 )
            
            $ pip install requests
            
            from requests import exceptions
            import argparse
            import requests
            import cv2
            import os
            # construct the argument parser and parse the arguments
            ap = argparse.ArgumentParser()
            ap.add_argument("-q", "--query", required=True, help="search query to search Bing Image API for") #쿼리는 "피캬츄"이렇게 입력
            ap.add_argument("-o", "--output", required=True, help="path to output directory of images") #내가 dataset저장할 위치
            args = vars(ap.parse_args())
            
            API_KEY = "INSERT_YOUR_API_KEY_HERE" #API에서 받은 KET값 넣어준다
            MAX_RESULTS = 400 #총 내가 다운받을 image개수
            GROUP_SIZE = 50 #page당 50개의 이미지
            URL = "https://api.cognitive.microsoft.com/bing/v7.0/images/search" #bing웹 브라우저에서 image/search에서 이미지 검색하는 것
            
            ''' 생략 '''
            # make the search
            print("[INFO] searching Bing API for '{}'".format(term))
            search = requests.get(URL, headers=headers, params=params) #검색 시작
            search.raise_for_status()
            # grab the results from the search, including the total number of
            # estimated results returned by the Bing API
            results = search.json() 
            estNumResults = min(results["totalEstimatedMatches"], MAX_RESULTS)
            print("[INFO] {} total results for '{}'".format(estNumResults,
                term))
    
        $ mkdir dataset/blue_jeans
        $ python search_bing_api.py --query "blue jeans" --output dataset/blue_jeans -> 이런식으로 명령어 입력해서 이미지 dataset다운받는다

    이 모듈을 이용해서 우리가 원하는 옷의 dataset을 구축해야된다.
    
3. smallVGGNet을 우리의 dataset으로 학습 시키기

    |CUDA 버전|NVIDA|CUDNN|Python 버전|Tensorflow 버전|Keras 버전| 추가 설치할 라이브러리|
    |------|---|---|---|---|---|
    |CUDA 10.1|418.x|CUDNN7.6.4|3.7 버전|2.2|2.4.3| openCV, request, 
    |테스트1|테스트2|테스트3|
    |테스트1|테스트2|테스트3|

