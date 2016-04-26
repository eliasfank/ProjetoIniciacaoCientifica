import sys
import cv2

MIN_MATCH_COUNT = 10

# Initiate SIFT detector
sift = cv2.xfeatures2d.SIFT_create()

images = {}
kp = {}
des = {}
bestImage = ""
bestLimear = 0

principal = cv2.imread(sys.argv[1])
kpPrincipal, desPrincipal = sift.detectAndCompute(principal,None)

for i in range(2,len(sys.argv)):
	imagePath = sys.argv[i]

	filename = imagePath[imagePath.rfind("/") + 1:]

	images[filename] = cv2.imread(sys.argv[i],0)

for i in images:
	kp[i], des[i] = sift.detectAndCompute(images[i],None)

for (i) in images:
	FLANN_INDEX_KDTREE = 0
	index_params = dict(algorithm = FLANN_INDEX_KDTREE, trees = 5)
	search_params = dict(checks = 50)

	flann = cv2.FlannBasedMatcher(index_params, search_params)

	matches = flann.knnMatch(desPrincipal,des[i],k=2)

	# store all the good matches as per Lowe's ratio test.
	good = []
	for m,n in matches:
	    if m.distance < 0.7*n.distance:
	        good.append(m)
	if(len(good)*100/len(kp[i]) > bestLimear):
		bestLimear = len(good)*100/len(kp[i])
		bestImage = i

print bestImage
print bestLimear