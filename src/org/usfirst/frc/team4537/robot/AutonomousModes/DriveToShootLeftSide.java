package org.usfirst.frc.team4537.robot.AutonomousModes;

import org.usfirst.frc.team4537.robot.Robot;

public class DriveToShootLeftSide extends Autonomous {
	
	private int stage = 0;

	public DriveToShootLeftSide(Robot robot)
	{
		super(robot);
		this.stage = 0;
	}

	public void update() {
		switch (stage) {
         	case 0:  
         		robot.getDriveBase().driveForwardForTime(0.5, 3.5);
         		if (robot.getDriveBase().isDriving()) {
         			stage = 1;
         		}
                break;
         	case 1:
         		// drive forward to range
         		break;
         	case 2:
         		// turn to angle;
         		break;
         	case 3:
         		// drive to range
         		break;
         	case 4:
         		// shoot low goal
         		break;
		}
		
		robot.getDriveBase().stop();
	}
}
