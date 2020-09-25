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

    3-1. 사전 준비
    
    |구분|가상환경 name|CUDA 버전|NVIDA|CUDNN|Python 버전|Tensorflow 버전|Keras 버전| 추가 설치할 라이브러리|
    |----|----|------|---|---|---|---|---|---|
    |학교컴1(효섭)|py356|CUDA 10.1|418.x|CUDNN7.6.4|3.7.7 버전|2.2|2.4.3| openCV(opencv-python), request, matplotlib, numpy, imutils, sklearn
    |학교컴1(주영)|paper|CUDA 10.1|418.x| ?? |3.7.7 버전|2.0| 2.1.3| openCV(opencv-python), request, matplotlib, numpy, imutils, sklearn
    
    //conda install로 안되는건 pip install 사용<br>
    3-2. train.py
    
        EPOCHS = 75    
        INIT_LR = 1e-3  #learning rate로 1e-3값은 Adam optimizer
        BS = 32 #batch size값
        IMAGE_DIMS = (96, 96, 3) #image는 96*96에 3개의 channel(r,g,b)포함함
        
        # 이부분들은 image dataset을 loading하고 loading dataset들 랜덤으로 섞음[loading한 dataset들은 각 카테고리 폴더에 저장되어 있어 이것을 shuffle한다는 의미]
        print("[INFO] loading images...")
        imagePaths = sorted(list(paths.list_images(args["dataset"])))
        random.seed(42)
        random.shuffle(imagePaths)

        # initialize the data and labels
        data = []
        labels = []
        
        # ImagePaths(loading한 dataset안에 폴더들)돌면서 데이터 전처리 과정 진행하고 multi-class-labels추출!!
        for imagePath in imagePaths:
            image = cv2.imread(imagePath)
            image = cv2.resize(image, (IMAGE_DIMS[1], IMAGE_DIMS[0])) #image는 96*96사이즈로 resize
            image = img_to_array(image) #image -> array
            data.append(image) #image 정보[array로 이루어져있음]를 data에 넣기

            #우리는 dataset안에 폴더들 이름을 "color_옷카테고리명"으로 지정하였다 [ex "red_longDress"] => 따라서 _를 기준으로 split하면 앞에는 color 뒤에는 카테고리이름을  multi-label로 지정할 수 있다
            l = label = imagePath.split(os.path.sep)[-2].split("_") 
            labels.append(l) # labels = [("red","longDress"),("blue","jeans")]이런식으로 저장됨
            
        # data의 array의 값을 모두 [0, 1]로 정규화하는 과정 [값이 크면 계산량 크기 때문에 -> 정규화를 해서 계산 간단 및 계산량 줄임]
        data = np.array(data, dtype="float") / 255.0 # array -> numpy array[정규화한 (0,1)]
        labels = np.array(labels) #label -> numpy array로 변환
        print("[INFO] data matrix: {} images ({:.2f}MB)".format(len(imagePaths), data.nbytes / (1024 * 1000.0)))
        
        
        # scikit-learn library’s의 MultiLabelBinarizer를 사용해서 multi label을 분류한다 
        """
            예제  labels=[("red","longDress"),("blue","jeans"), ("blue", "longDress"),("red","jeans")]이면
            mlb.classes_
                >> array(['red'.'blue','longDress','jeans'], dtype=object)
            mlb.fit_transform([('red', 'jeans')])
                >> array([[1,0,0,1]])
                
        """
        print("[INFO] class labels:")
        mlb = MultiLabelBinarizer()
        labels = mlb.fit_transform(labels) #two-hot encoding 사용(two-hot encoding은 1이 두번 나오기 때문)

        # loop over each of the possible class labels and show them
        for (i, label) in enumerate(mlb.classes_):
            print("{}. {}".format(i + 1, label))

        # data의 80%는 traing용, 20%는 test용
        (trainX, testX, trainY, testY) = train_test_split(data, labels, test_size=0.2, random_state=42)
        #Keras에서 제공하는 패키지 "ImageDataGenerator"로 image data학습을 쉽게하도록 해준다
        """
            ImageDataGenerator class를 통해 객체 생성할때 파라미터를 전달 -> 데이터 전처리를 쉽게 할 수 있다 => [이미지를 움직여 1장의 사진을 움직여 여러장의 사진 늘리기 => 작은 데이터셋을 가지고 큰 데이터셋으로 늘리는 방법이라고 생각하기 => class당 1000개보다 적은 이미지 데이터셋일 경우 효율적임]
            👉[참고사이트1](https://m.blog.naver.com/PostView.nhn?blogId=isu112600&logNo=221582003889&proxyReferer=https:%2F%2Fwww.google.com%2F )
            👉[참고사이트2](https://keraskorea.github.io/posts/2018-10-24-little_data_powerful_model/)
            
            width_shift_range = 이미지를 왼쪽 오른쪽으로 움직인다
            height_shift_range =  이미지를 아래, 위로 움직인다
            horizontal_flip = 이미지를 위, 아래로 뒤집는다
            rotation_range = 이미지를 랜덤으로 회전시킨다
            zoom_range = 이미지를 zoom한다
            shear_range = 이미지를 전단 변환한다
        """
        aug = ImageDataGenerator(rotation_range=25, width_shift_range=0.1,
            height_shift_range=0.1, shear_range=0.2, zoom_range=0.2,
            horizontal_flip=True, fill_mode="nearest")
           
        
        # smallerVGGNet 모델 build한다[모델은 /paimage/SmallerVGGNet.py에 존재]
        #finalAct="sigmoid" => 활성화 함수로 "sigmod"함수 설정 => multi-label 분류에서는 활성화 함수로 sigmod를 사용한다
        👉[참고사이트3](https://www.kaggle.com/c/imet-2019-fgvc6/discussion/89823)
        👉[optimizer 설명1](https://forensics.tistory.com/28)
        👉[optimizer 설명2] (https://wikidocs.net/36033)
        print("[INFO] compiling model...")
        model = SmallerVGGNet.build(
            width=IMAGE_DIMS[1], height=IMAGE_DIMS[0],
            depth=IMAGE_DIMS[2], classes=len(mlb.classes_),
            finalAct="sigmoid")
        # optimizer Adam 사용
        opt = Adam(lr=INIT_LR, decay=INIT_LR / EPOCHS) #decay: learning rate schedule이다
        
        
        # 모델 구성한다(compile)
        # loss function으로 binary_crossentropy 사용 => 각 출력 레이블을 독립적 인 Bernoulli 분포로 취급하는 것이므로 각 출력 노드에 독립적으로 불이익주기 위해
        ## 다시 공부 이해 x 왜 categorical cross-entropy 사용안하는지 이해 x 
        model.compile(loss="binary_crossentropy", optimizer=opt, metrics=["accuracy"])

        # 모델 훈련(fit)
        print("[INFO] training network...")
        H = model.fit_generator(
            aug.flow(trainX, trainY, batch_size=BS),
            validation_data=(testX, testY),
            steps_per_epoch=len(trainX) // BS,
            epochs=EPOCHS, verbose=1)
         
        #모델 저장 -> keras는 h5모델로 저장됨(참고)
        print("[INFO] serializing network...")
        model.save(args["model"])
        
        # mlb.labelbin pickle파일 생성 => 위에서 만든 mlb class내용을 저장하고 classify에서 어떤 label에 해당되는지 확인작업을 위해서 필요
        print("[INFO] serializing label binarizer...")
        f = open(args["labelbin"], "wb")
        f.write(pickle.dumps(mlb))
        f.close()
        
        
        # 이부분은 이제 내가 훈련 과정을 그래프로 표현하기 위해 matplotlib를 사용함 -> 필요 -> 시각적으로 확인하는 것이 더 정확함
        # 현재 이부분 오류 있는데 변수 명 오류인 듯 
        plt.style.use("ggplot")
        plt.figure()
        N = EPOCHS
        plt.plot(np.arange(0, N), H.history["loss"], label="train_loss")
        plt.plot(np.arange(0, N), H.history["val_loss"], label="val_loss")
        plt.plot(np.arange(0, N), H.history["acc"], label="train_acc") #이부분 오류 acc 아님 accuracy임 수정할것
        plt.plot(np.arange(0, N), H.history["val_acc"], label="val_acc")
        plt.title("Training Loss and Accuracy")
        plt.xlabel("Epoch #")
        plt.ylabel("Loss/Accuracy")
        plt.legend(loc="upper left")
        plt.savefig(args["plot"])

        
        $ python train.py --dataset ./dataset --model fashion.model --labelbin mlb.pickle
        
4. 옷 분류하기 => 옷 예측하는 부분

    4-1. classify.py
        
        # 옷 분류하려는 사진 불러오는 부분
        image = cv2.imread(args["image"])
        output = imutils.resize(image, width=400)
        
        # train에서 image 전처리 하는 것과 같이 똑같이 setting
        image = cv2.resize(image, (96, 96))
        image = image.astype("float") / 255.0
        image = img_to_array(image)
        image = np.expand_dims(image, axis=0)
        
        # model이랑 label 저장한거 불러오기
        print("[INFO] loading network...")
        model = load_model(args["model"])
        mlb = pickle.loads(open(args["labelbin"], "rb").read())
        
        # model.predict -> 내 image파일을 모델의 input으로 넣어 어떤 색과 카테고리인지 예측
        # 예측 결과는 array([[1,0,0,1]]) 이런 것처럼 two-hot encoding된 array 결과가 나온다. 1이 들어간 index위치를 얻어 idxs에 넣는다
        print("[INFO] classifying image...")
        proba = model.predict(image)[0]
        idxs = np.argsort(proba)[::-1][:2]

        # 이부분 필요 없음 -> 사진에 뭐랑 뭐가 높인지 띄어즈는 거 뿐
        for (i, j) in enumerate(idxs):
        # build the label and draw the label on the image
        label = "{}: {:.2f}%".format(mlb.classes_[j], proba[j] * 100)
        cv2.putText(output, label, (10, (i * 30) + 25), 
            cv2.FONT_HERSHEY_SIMPLEX, 0.7, (0, 255, 0), 2)
            
        # 각 label들에 대해 몇 %로 높은지 출력해줌 -> 이게 필요 옷 분류하는 거니까
        for (label, p) in zip(mlb.classes_, proba):
            print("{}: {:.2f}%".format(label, p * 100))
            
        # 필요 x
        cv2.imshow("Output", output)
        cv2.waitKey(0)
        
        
        $ python classify.py --model fashion.model --labelbin mlb.pickle --image examples/example_01.jpg
