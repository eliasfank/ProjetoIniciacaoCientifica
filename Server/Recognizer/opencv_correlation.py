#!/usr/bin/python
# import the necessary packages
import sys
from scipy.spatial import distance as dist
import matplotlib.pyplot as plt
import numpy as np
import argparse
import glob
import cv2

# initialize the index dictionary to store the image name
# and corresponding histograms and the images dictionary
# to store the images themselves
index = {}
images = {}
# loop over the image paths
for i in range(1,len(sys.argv)):
	imagePath = sys.argv[i]
	# extract the image filename (assumed to be unique) and
	# load the image, updating the images dictionary
	filename = imagePath[imagePath.rfind("/") + 1:]
	image = cv2.imread(imagePath)
	images[filename] = cv2.cvtColor(image, cv2.COLOR_BGR2RGB)
	
	# extract a 3D RGB color histogram from the image,
	# using 8 bins per channel, normalize, and update
	# the index
	hist = cv2.calcHist([image], [0, 1, 2], None, [8, 8, 8],
		[0, 256, 0, 256, 0, 256])
	hist = cv2.normalize(hist).flatten()
	index[filename] = hist
	if(i == 1):
			principal = filename

#Start Method
methodName = "Correlation"
method = cv2.cv.CV_COMP_CORREL
# initialize the results dictionary and the sort
# direction
results = {}
reverse = False

# if we are using the correlation or intersection
# method, then sort the results in reverse order
if methodName in ("Correlation", "Intersection"):
	reverse = True

# loop over the index
for (k, hist) in index.items():
	# compute the distance between the two histograms
	# using the method and update the results dictionary
	d = cv2.compareHist(index[principal], hist, method)
	results[k] = d

# sort the results
results = sorted([(v, k) for (k, v) in results.items()], reverse = reverse)

# loop over the results
for (i, (v, k)) in enumerate(results):
	# show the result
	#ax = fig.add_subplot(1, len(images), i + 1)
	#ax.set_title("%s: %.2f" % (k, v))
	#plt.imshow(images[k])
	#plt.axis("off")
	if(i==1):
		print(k)
		print(v)
		


