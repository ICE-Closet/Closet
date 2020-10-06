import Leap, sys, thread, time
from Leap import CircleGesture, KeyTapGesture, ScreenTapGesture, SwipeGesture

class LeapMotionListener(Leap.Listener):
    finger_name = ['Thumb','Index', 'Middle', 'Ring', 'Pinky']
    bone_names = ['Metacarpal', 'Proximal', 'Intermediate', 'Distal']
    state_names = ['STATE_INVALID', 'STATE_START', 'STATE_UPDATE', 'STATE_END']

    def on_init(self, controller):
        print "Initialized"

    def on_connect(self, controller):
        print "Motion Sensor Connected!"

        controller.enable_gesture(Leap.Gesture.TYPE_CIRCLE);
        controller.enable_gesture(Leap.Gesture.TYPE_KEY_TAP);
        controller.enable_gesture(Leap.Gesture.TYPE_SCREEN_TAP);
        controller.enable_gesture(Leap.Gesture.TYPE_SWIPE);

    def on_disconnect(self, controller):
        print "Motion Sensor Disconnected"

    def on_exit(self, controller):
        print "Exited"

    def on_frame(self, controller):
        frame = controller.frame()

        for gesture in frame.gestures():
            if gesture.type == Leap.Gesture.TYPE_CIRCLE:
                circle = CircleGesture(gesture)

                if circle.pointable.direction.angle_to(circle.normal) <= Leap.PI/2:
                    clockwiseness = "clockwise"
                else:
                    clockwiseness = "counter-clockwise"

                swept_angle = 0
                if circle.state != Leap.Gesture.STATE_START:
                    previous = CircleGesture(controller.frame(1).gesture(circle.id))
                    swept_angle = (circle.progress - previous.progress)*2*Leap.PI

                    print "ID: " + str(circle.id) + " Progress: " + str(circle.progress) + " Radius: " + str(circle.radius) + " Swept Angle: " + str(swept_angle * Leap.RAD_TO_DEG) + " " +clockwiseness

                if gesture.type == Leap.Gesture.TYPE_SWIPE:
                    swipe = SwipeGesture(gesture)
                    print "Swipe ID: " + str(swipe.id) + " State: " + self.state_names[gesture.state] + " Position: " + str(swipe.position) + " Direction: " + str(swipe.direction) + " Speed (mm/s): " + str(swipe.speed)

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
