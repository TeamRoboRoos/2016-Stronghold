package org.usfirst.frc.team4537.robot.AutonomousModes;

import org.usfirst.frc.team4537.robot.Robot;

public abstract class Autonomous {
	
	public Robot robot;
	
	public Autonomous(Robot robot){
		this.robot = robot;
	}

	public abstract void update();
}















