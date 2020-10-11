# import the necessary packages
from tensorflow.keras.preprocessing.image import img_to_array
from tensorflow.keras.models import load_model
import numpy as np
import imutils
import pickle
import cv2

class ClothesML():
    def __init__(self):
        self.pattern = {"flower": 0, "check": 0, "nike": 0, "none": 0, "stripe": 0}
        self.color = {"black": 0, "gray": 0, "blue": 0, "red": 0, "white": 0, "yellow": 0, "khaki": 0, "brown": 0,
                 "beige": 0, "colornone": 0, "pink": 0, "purple": 0, "lightblue": 0}
        self.clothes = {"longsleeve": 0, "longshirt": 0, "longneat": 0, "shortsleeve": 0, "shortshirt": 0, "sleeveless": 0,
                   "longpants": 0, "shortpants": 0, "shortskirt": 0, "longskirt": 0, "adidaslongpants": 0,
                   "adidasshortpants": 0, "joggerpants": 0, "winlongdress": 0, "winshortdress": 0, "sumshortdress": 0,
                   "sumlongdress": 0, "cardigan": 0, "blazer": 0, "ma1": 0, "neatvest": 0, "riderjacket": 0, "parka": 0,
                   "coat": 0, "hoodie": 0, "jacket": 0}

    def classify(self, img):
        #load image
        image = cv2.imread(img)
        output = imutils.resize(image, width=400)

        #pre-process image
        image = cv2.resize(image, (96, 96))
        image = image.astype("float") / 255.0
        image = img_to_array(image)
        image = np.expand_dims(image, axis=0)

        #load fashion model
        print("[INFO] loading network...")
        model = load_model("/home/dasan/keras-multi-label/fashion10.model", compile=False)

        #load pickle file
        mlb = pickle.loads(open("/home/dasan/keras-multi-label/mlb10.pickle", "rb").read())

        # classifying image
        print("[INFO] classifying image...")
        proba = model.predict(image)[0]
        idxs = np.argsort(proba)[::-1][:2]

        # show the probabilities for each of the individual labels
        for (label, p) in zip(mlb.classes_, proba):
            if label in ("flower", "check", "nike", "none", "stripe"):
                self.pattern[label] = p * 100
                continue
            if label in (
            "black", "gray", "blue", "red", "white", "yellow", "khaki", "brown", "beige", "colornone", "pink", "purple",
            "lightblue"):
                self.color[label] = p * 100
                continue
            if label in (
            "longsleeve", "longshirt", "longneat", "shortsleeve", "shortshirt", "sleeveless", "longpants", "shortpants",
            "shortskirt", "longskirt", "adidaslongpants", "adidasshortpants", "joggerpants", "winlongdress",
            "winshortdress", "sumshortdress", "sumlongdress", "cardigan", "blazer", "ma1", "neatvest", "riderjacket",
            "parka", "coat", "hoodie", "jacket"):
                self.clothes[label] = p * 100
                continue
        Rcolor = sorted(self.color.items(), key=lambda x: x[1], reverse=True)
        Rpattern = sorted(self.pattern.items(), key=lambda x: x[1], reverse=True)
        Rclothes = sorted(self.clothes.items(), key=lambda x: x[1], reverse=True)

        return Rcolor[0][0] + "_" + Rpattern[0][0] + "_" + Rclothes[0][0]

