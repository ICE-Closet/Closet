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