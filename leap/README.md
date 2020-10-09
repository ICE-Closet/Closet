### leap motion

#### reference: https://developer-archive.leapmotion.com/documentation/python/devguide/Leap_SDK_Overview.html

- On Widnows OS
  
    사용한 python version: 2.7.5
    필요한 파일: Leap.py, LeapPyhon.pyd, Leap.lib, LeapPython.dll
    실행 예제 파일: Sample.py
    테스트 코드: leapdirection.py
    
- On Raspbian OS

  사용한 python version: 2.7.5
  필요한 파일: Leap.py, LeapPyhon.pyd, Leap.lib, LeapPython.so
  실행 예제 파일: Sample.py
  테스트 코드: ??
  

##### Leap motion을 이용하여 direction detection
[leapdirection.py]

``` python
import Leap, sys, math
from Leap import CircleGesture, KeyTapGesture, ScreenTapGesture, SwipeGesture
from Leap import SwipeGesture

class LeapMotionListener(Leap.Listener):
    def on_connect(self, controller):
        print "Motion Sensor Connected!"
        controller.enable_gesture(Leap.Gesture.TYPE_SWIPE)

    def on_frame(self, controller):
        frame = controller.frame()

        for gesture in frame.gestures():
            if gesture.type == Leap.Gesture.TYPE_SWIPE:
                swipe = SwipeGesture(gesture)
                swipeDir = swipe.direction

                if (swipeDir.x>0 and math.fabs(swipeDir.x) > math.fabs(swipeDir.y)):
                    print "Swiped right"
                elif (swipeDir.x<0 and math.fabs(swipeDir.x) > math.fabs(swipeDir.y)):
                    print "Swiped left"

def main():
    listener = LeapMotionListener()
    controller = Leap.Controller()

    controller.add_listener(listener)

    print "Press enter to quit"

    try:
        sys.stdin.readline()
    except KeyboardInterrupt:
        pass
    finally:
        controller.remove_listener(listener)

if __name__ == "__main__":
    main()

```
