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

for i in range(0,2):
	#image = cv2.imread("/home/dasan/Downloads/man/e"+str(i)+".jpg") #/home/dasan/Downloads/man/b
	image = cv2.imread("/home/dasan/Downloads/vee.jpg") #/home/dasan/Downloads/man/b	
	output = imutils.resize(image, width=400)
	 

	image = cv2.resize(image, (96, 96))
	image = image.astype("float") / 255.0
	image = img_to_array(image)
	image = np.expand_dims(image, axis=0)


	print("[INFO] loading network...")
	model = load_model("/home/dasan/keras-multi-label/fashion10.model",compile=False)

	mlb = pickle.loads(open("/home/dasan/keras-multi-label/mlb10.pickle", "rb").read())


	print("[INFO] classifying image...")
	proba = model.predict(image)[0]
	idxs = np.argsort(proba)[::-1][:2]
	# loop over the indexes of the high confidence class labels
	for (i, j) in enumerate(idxs):
		# build the label and draw the label on the image
		label = "{}: {:.2f}%".format(mlb.classes_[j], proba[j] * 100)
		cv2.putText(output, label, (10, (i * 30) + 25), 
			cv2.FONT_HERSHEY_SIMPLEX, 0.7, (0, 255, 0), 2)

	# show the probabilities for each of the individual labels
	for (label, p) in zip(mlb.classes_, proba):
		print("{}: {:.2f}%".format(label, p * 100))



