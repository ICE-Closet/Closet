# SMARTICE Closet
# RaspberryPi, Sensors, MachineLearning을 이용한 SMARTICE Closet
# SMARTICE Closet MOBILE APPLICATION

[![GitHub forks](https://img.shields.io/github/forks/ICE-Closet/Closet)](https://github.com/ICE-Closet/Closet/network) [![GitHub stars](https://img.shields.io/github/stars/ICE-Closet/Closet)](https://github.com/ICE-Closet/Closet/stargazers) [![GitHub license](https://img.shields.io/github/license/ICE-Closet/Closet)](https://github.com/ICE-Closet/Closet/blob/master/LICENSE) [![AWS](https://img.shields.io/badge/Amazon%20AWS-%23232F3E?logo=amazon-aws&logoColor=white)](https://aws.amazon.com/ko/free/?trk=ps_a134p000003yHYmAAM&trkCampaign=acq_paid_search_brand&sc_channel=PS&sc_campaign=acquisition_KR&sc_publisher=Google&sc_category=Core-Main&sc_country=KR&sc_geo=APAC&sc_outcome=acq&sc_detail=aws&sc_content=Brand_Core_aws_e&sc_segment=444218215904&sc_medium=ACQ-P|PS-GO|Brand|Desktop|SU|Core-Main|Core|KR|EN|Text&s_kwcid=AL!4422!3!444218215904!e!!g!!aws&ef_id=CjwKCAjwiaX8BRBZEiwAQQxGx2XOkTu_Tu8g0unvps-CYtT0OWOZFI3dswH1GM3tlS3TZ_c9zjO8SRoCa84QAvD_BwE:G:s&s_kwcid=AL!4422!3!444218215904!e!!g!!aws) [![Django](https://img.shields.io/badge/Django-Python-orange?logo=Django)](https://www.djangoproject.com/) [![Android](https://img.shields.io/badge/Android-Kotlin-3DDC84?logo=android&logoColor=white)](https://developer.android.com/?hl=ko) [![DeepLearning](https://img.shields.io/badge/tensorflow-Python-orange?logo=tensorflow&logoColor=white)](http://tensorflow.org/)

자동으로 자신의 옷장안에 있는 옷들의 상태(옷장안에 옷의 유무)를 체크
해주고, 현재 옷장안에 있는 옷을 기반으로 개인 스타일 맞춤형 코디 서비스를
제공하기 위하여 이 서비스 제공해준다.
<br><br>

### **Team SMARTICE**
<br>

- 팀장
    - 박주영
- 팀원
    - 이재성, 이유진, 이효섭

[![N|Solid](https://i.ibb.co/qCwrBHZ/qq.png)](https://github.com/ICE-Closet/Closet/graphs/contributors)

## **SMARTICE Project**
### **Usecase Diagram**


<br>
<br>

**1. Backend Server** - Django ([API_SERVER](https://github.com/ICE-Closet/Closet/tree/API_Server))
> 주요 기능
>   - [Check clothes status](https://github.com/ICE-Closet/Closet/blob/API_Server/Closet/accounts/views/clothes_info.py): 라즈베리파이로 부터 옷의 정보와 옷의 상태를
전달받으면 해당옷을 db에서 찾아 상태를 업데이트한다.
>   - [Recommendation](https://github.com/ICE-Closet/Closet/tree/API_Server/Closet/accounts/Recommendation_algo): 사용자가 선택한 스타일, 색상 과 날씨정보를 기반으로 옷을 추천한다(이때 추천은 5.Recommendation Algorithms에서 설계한 알고리즘 기반으로 실행하였다)
<br>

**2. Mobile Application** - Android ([Android_Studio](https://github.com/ICE-Closet/Closet/tree/Android_Studio))
> 주요 기능
>   - Take a picture of clothes: 사용자가 자신의 옷을 등록하기 위해 옷 사진을 촬영한다.
>   - Browse clothes: 사용자가 촬영하고 저장한 옷을 카테고리(상의, 하의, 아우터, 드레스)별로 보여준다.
>   - Show frequency worn clothes: 사용자가 자주 입었던 카테고리 별 옷을 보여준다.
>   - Recommend clothes: 사용자는 스타일과 원하는 색을 선택하여 옷 추천 서비스를 제공받는다
<br>

**3. Deep Learning** - ML([ML](https://github.com/ICE-Closet/Closet/tree/ML) & [hyoseop](https://github.com/ICE-Closet/Closet/tree/hyoseop))
> 주요 기능
>   - [Communicate with other](https://github.com/ICE-Closet/Closet/blob/ML/socketCommuication.py): 머신러닝 서버는 app과 라즈베리파이와 socket 통신으로 데이터를 주고받는다
>   - [Classify clothes](https://github.com/ICE-Closet/Closet/blob/ML/MLclassify.py): 앱이나 라즈베리파이로 부터 옷의 사진을 받으면 해당 옷을 분류하여(색_패턴_카테고리) 서버로 전송한다.
>   - [Model train](https://github.com/ICE-Closet/Closet/blob/hyoseop/keras-multi-label/train.py): 3가지 카테고리를 분류하는 딥러닝 모델(smallVGGNet model 기반)으로 학습하여 우리만의 모델을 저장한다.
<br>

**4. Raspberry pi and sensors control** - Raspberrypi & Sensor ([raspberry](https://github.com/ICE-Closet/Closet/tree/raspberry))
> 주요 기능
>   - [Clothes in/out check](https://github.com/ICE-Closet/Closet/blob/raspberry/final/closet_functions.py): Light sensor로 옷장문의 개폐를 확인하고, 옷장 문이 열렸으면 Ultrasonic sensor 와 카메라가 작동한다. 사용자가 옷을 넣는지 빼는지 Ultrasonic sensor로
감지하고, 카메라로 찍은 옷의 사진을 ML server로 전송한다.

<br>

**5. Recommendation Algorithms** - Recommendation ([Recommendation_algorithms](https://github.com/ICE-Closet/Closet/tree/Recommendation_algorithms))
> 주요 기능
>   - [추천 알고리즘 설계](https://github.com/ICE-Closet/Closet/tree/Recommendation_algorithms/_clothes_recommendation)









