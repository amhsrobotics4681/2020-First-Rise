import math
"""For constant velocity, speed should be 15, deltaY should be 1.5"""
mode = input("Which is constant? Angle or Velocity\n")
if mode == "Angle":
    constantAngle = int(input("What is the angle? (0-90 (int))\n"))
    deltaY = input("What is the displacement in the Y direction\n")
    deltaX = input("What is the displacement in the X direction\n")
    print("Possilbe launch velocities are...")
    anyAngles = False
    for i in range(20, 60):
            velocity = i/4
            xSpeed = velocity*math.cos(math.radians(constantAngle))
            ySpeed = velocity*math.sin(math.radians(constantAngle))
            time = float(deltaX)/xSpeed
            yDistance = ySpeed*time + .5*(-9.8)*(time*time)
            anyAngles = True
            if yDistance < 1.8 and yDistance > 1.6:
                print("Velocity: "+str(velocity)+"m/s , Time: "+str(time)+", Height: "+str(yDistance))
    if anyAngles == False:
        print("Sorry, no velocities at this distance")
if mode == "Velocity":
    constantVelocity = input("What is the velocity? (Meters/Second (int))\n")
    deltaY = input("What is the displacement in the Y direction\n")
    deltaX = input("What is the displacement in the X direction\n")
    print("Possilbe launch angles are...")
    anyAngles = False
    for i in range(0,90):
        xSpeed = int(constantVelocity)*math.cos(math.radians(i))
        ySpeed = int(constantVelocity)*math.sin(math.radians(i))
        time = float(deltaX)/xSpeed
        yDistance = ySpeed*time + .5*(-9.8)*(time*time)
        if yDistance < 1.8 and yDistance > 1.6:
            anyAngles = True
            print("Angle: "+str(i)+", Time: "+str(time)[0:5]+", Height: "+str(yDistance)[0:5])
    if anyAngles == False:
        print("Sorry, no angles at this distance")
