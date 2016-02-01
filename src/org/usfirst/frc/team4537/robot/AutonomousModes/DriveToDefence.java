package org.usfirst.frc.team4537.robot.AutonomousModes;

import org.usfirst.frc.team4537.robot.Robot;

public class DriveToDefence extends Autonomous {
	
	private int stage = 0;
	
	public DriveToDefence(Robot robot)
	{
		super(robot);
	}

	public void update() {
		switch (stage) {
     	case 0:  
     		robot.getDriveBase().driveForwardForTime(0.5, 3.5);
     		if (robot.getDriveBase().isDriving()) {
     			stage = 1;
     		}
            break;
		}
	}
}
