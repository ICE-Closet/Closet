import requests
import json

port = 30000


def db_store(img,result, token):#img,result, token
    URL = "http://13.124.208.47:8000/accounts/clothes_info/"
    headers = { 'Authorizations' : token}
    files = {
        'image': open(img, 'rb'),
    }
    data = {
        "classify": result,
    }
    try:
        response = requests.post(url=URL, headers=headers, data=data, files=files)
        print(response)

    except Exception as e:
        print(e)

if __name__ == '__main__':
    token = 'eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyIjoyfQ.mP_IOdB4LEsJIeeUVaxLpG0k4NlnMesaMhU13J6gQ8M'
#eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyIjo0Mn0.uV71npHImBM1kB8gF4vDGKX4Wi7V65A1tay3PsdnJhw ->woman
#eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyIjoyfQ.mP_IOdB4LEsJIeeUVaxLpG0k4NlnMesaMhU13J6gQ8M  -> man
    l3 = ['beige_none_longshirt','black_none_longshirt']
    li =['black_none_sumshortdress','black_none_shortsleeve','black_stripe_longsleeve','red_none_longsleeve','beige_none_cardigan','beige_none_longpants','black_none_longshirt','lightblue_none_shortpants','beige_none_shortskirt','black_none_winshortdress','pink_none_longsleeve','black_none_riderjacket']
    l2 = ['black_none_cardigan','red_check_longshirt','black_none_longpants','kakhi_none_ma1','beige_none_longpants','black_none_riderjacket','red_none_longsleeve']
    for i in range(12,14):
        db_store("/home/dasan/Downloads/man/e"+str(i)+".jpeg", l3[i-12],token)
