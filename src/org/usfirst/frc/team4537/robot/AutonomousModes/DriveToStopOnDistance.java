package org.usfirst.frc.team4537.robot.AutonomousModes;

import org.usfirst.frc.team4537.robot.Robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveToStopOnDistance extends Autonomous {
	
	private int stage = 0;

	public DriveToStopOnDistance(Robot robot)
	{
		super(robot);
		this.stage = 0;
        SmartDashboard.putString("DB/String 3", "Driving Forward for Distance");
	}

	public void update() {
		switch (stage) {
     	case 0:  
     		robot.getDriveBase().driveForwardForDistance(0.15, 50);
     		if (!robot.getDriveBase().isDriving()) {
     			stage = 1;
     		}
            break;
     	case 1:
            SmartDashboard.putString("DB/String 3", "2 Points");
     		robot.getDriveBase().stop();
     		stage = 2;
            break;
	}
	}
}
