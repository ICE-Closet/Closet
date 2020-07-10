import RPi.GPIO as GPIO
import time
import datetime

GPIO.setmode(GPIO.BCM)
GPIO.setwarnings(False)

TRIG = 20
ECHO = 21
TRIG1 = 23
ECHO1 = 24

dis1, dis2= 0, 0

def sensing():
    dis3, dis4 =20,20
    f_count1, f_count2 = 0, 0
    now1,now2 = datetime.time(12, 23, 10), datetime.time(12, 23, 50)
    while (1):
        #print("waiting for sensor 1 to send siginal")
        time.sleep(0.05)
        GPIO.output(TRIG, True)
        time.sleep(0.00001)
        GPIO.output(TRIG, False)
        #print("reading sensor 1")
        while GPIO.input(ECHO) == 0:
            pulse_start = time.time()
        while GPIO.input(ECHO) == 1:
            pulse_end = time.time()
        pulse_duration = pulse_end - pulse_start
        dis1 = pulse_duration * 17150
        dis1 = round(dis1, 2)
        # distance1[0] = distance1[1]
        # distance[1] = dis1
        print("distance sensor1: ", dis1, "cm")
        #print("waiting for sensor 2 to send signal")
        time.sleep(0.05)
        GPIO.output(TRIG1, True)
        time.sleep(0.00001)
        GPIO.output(TRIG1, False)
        #print("reading sensor 2")
        while GPIO.input(ECHO1) == 0:
            pulse_start2 = time.time()
        while GPIO.input(ECHO1) == 1:
            pulse_end2 = time.time()
        pulse_duration2 = pulse_end2 - pulse_start2
        dis2 = pulse_duration2 * 17150
        dis2 = round(dis2, 2)
        #distance2[0] = distance[1]
        #distance2[1] = dis2
        print("distance sensor2: ", dis2, "cm")
        #GPIO.cleanup()

        if dis1 <= 10:
            now1 = datetime.datetime.now()
            time.sleep(1)
        if dis2 <= 10:
            now2 = datetime.datetime.now()
            time.sleep(1)
        print("f",f_count1,f_count2,"\n")
        print("t", now2.second - now1.second,"\n")
        if 1<= now2.second - now1.second <= 3:
            f_count1,f_count2 =0,0
            now1,now2 = datetime.time(12, 23, 10), datetime.time(12, 23, 50)
            print("right\n")
        if 1<= now1.second - now2.second <= 3:
            f_count1,f_count2 =0,0
            now1,now2 = datetime.time(12, 23, 10), datetime.time(12, 23, 50)
            print("left\n")


if __name__ == "__main__":
    print("Distance measure")
    GPIO.setup(TRIG, GPIO.OUT)
    GPIO.setup(ECHO, GPIO.IN)
    GPIO.setup(TRIG1, GPIO.OUT)
    GPIO.setup(ECHO1, GPIO.IN)
    GPIO.output(TRIG, False)
    GPIO.output(TRIG1, False)

    sensing()

