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
            ap.add_argument("-q", "--query", required=True,
                help="search query to search Bing Image API for")
            ap.add_argument("-o", "--output", required=True,
                help="path to output directory of images")
            args = vars(ap.parse_args())
    
        $ mkdir dataset/charmander<br>

    이 모듈을 이용해서 우리가 원하는 옷의 dataset을 구축해야된다.
3. smallVGGNet을 우리의 dataset으로 학습 시키기


