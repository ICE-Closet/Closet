# USAGE
# python classify.py --model fashion.model --labelbin mlb.pickle --image examples/example_01.jpg

# import the necessary packages
from tensorflow.keras.preprocessing.image import img_to_array
from tensorflow.keras.models import load_model
import numpy as np
import argparse
import imutils
import pickle
import cv2
import os

def classifyimg(img):
	pattern={"flower":0, "check":0, "nike":0, "none":0, "stripe":0}
	color={"black":0, "gray":0, "blue":0, "red":0, "white":0, "yellow":0, "khaki":0, "brown":0, "beige":0,"colornone":0,"pink":0,"purple":0,"lightblue":0}
	clothes={"longsleeve":0, "longshirt":0, "longneat":0, "shortsleeve":0, "shortshirt":0, "sleeveless":0, "longpants":0, "shortpants":0, "shortskirt":0, "longskirt":0, "adidaslongpants":0, "adidasshortpants":0, "joggerpants":0, "winlongdress":0, "winshortdress":0, "sumshortdress":0, "sumlongdress":0, "cardigan":0, "blazer":0, "ma1":0, "neatvest":0, "riderjacket":0, "parka":0, "coat":0, "hoodie":0, "jacket":0}

        # load the image
	#image = cv2.imread(args["image"])
	image = cv2.imread(img)
	output = imutils.resize(image, width=400)

	# pre-process the image for classification
	image = cv2.resize(image, (96, 96))
	image = image.astype("float") / 255.0
	image = img_to_array(image)
	image = np.expand_dims(image, axis=0)

	# load the trained convolutional neural network and the multi-label
	# binarizer
	print("[INFO] loading network...")
	#model = load_model(args["model"])
	model = load_model("/home/dasan/keras-multi-label/fashion6.model",compile=False)

	#mlb = pickle.loads(open(args["labelbin"], "rb").read())
	mlb = pickle.loads(open("/home/dasan/keras-multi-label/mlb6.pickle", "rb").read())

	# classify the input image then find the indexes of the two class
	# labels with the *largest* probability
	print("[INFO] classifying image...")
	proba = model.predict(image)[0]
	idxs = np.argsort(proba)[::-1][:2]

	# loop over the indexes of the high confidence class labels
	#for (i, j) in enumerate(idxs):
		# build the label and draw the label on the image
	#	label = "{}: {:.2f}%".format(mlb.classes_[j], proba[j] * 100)
	#	cv2.putText(output, label, (10, (i * 30) + 25), 
	#		cv2.FONT_HERSHEY_SIMPLEX, 0.7, (0, 255, 0), 2)

	# show the probabilities for each of the individual labels
	for (label, p) in zip(mlb.classes_, proba):
		if label in ("flower", "check", "nike", "none", "stripe"):
			pattern[label] = p*100
			continue 	
		if label in ("black", "gray", "blue", "red", "white", "yellow", "khaki", "brown","beige","colornone","pink","purple","lightblue"):
			color[label] = p*100
			continue 	
		if label in ("longsleeve", "longshirt", "longneat", "shortsleeve", "shortshirt", "sleeveless", "longpants", "shortpants", "shortskirt", "longskirt","adidaslongpants","adidasshortpants","joggerpants","winlongdress","winshortdress","sumshortdress","sumlongdress","cardigan","blazer","ma1","neatvest","riderjacket","parka","coat","hoodie","jacket"):
			clothes[label] = p*100
			continue 	

	t1= sorted(color.items(),key=lambda x:x[1], reverse=True)
	t2 = sorted(pattern.items(),key=lambda x:x[1], reverse=True)
	t3 = sorted(clothes.items(),key=lambda x:x[1], reverse=True)	
	#print(t1[0][0])	
	#print(t2[0][0])
	#print(t3[0][0])
	return t1[0][0]+"_"+t2[0][0]+"_"+t3[0][0] 	
	#print("{}: {:.2f}%".format(label, p * 100))	
	# show the output image
	#cv2.imshow("Output", output)
	#cv2.waitKey(0)
