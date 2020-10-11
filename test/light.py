import RPi.GPIO as GPIO
import time
import datetime

GPIO.setmode(GPIO.BCM)
GPIO.setwarnings(False)

resistorPin = 4

while True:
    print("Light measure")
    GPIO.setup(resistorPin, GPIO.OUT)
    GPIO.output(resistorPin, GPIO.LOW)
    time.sleep(0.1)

    GPIO.setup(resistorPin, GPIO.IN)
    currentTime = time.time()
    diff = 0

    while (GPIO.input(resistorPin) == GPIO.LOW):
        diff = time.time() - currentTime
    diff = diff * 100000
    print("light:", diff)
    time.sleep(0.4)
