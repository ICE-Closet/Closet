import cv2
CAM_ID = 0
def capture(camid = CAM_ID):
    cam = cv2.VideoCapture(camid)
    if cam.isOpened() == False:
        print ('cant open the cam (%d)' % camid)
        return None
    ret, frame = cam.read()
    if frame is None:
        print ('frame is not exist')
        return None
    cv2.imwrite('messigray.png',frame, params=[cv2.IMWRITE_PNG_COMPRESSION,0])
    cam.release()

if __name__ == '__main__':
    capture()
