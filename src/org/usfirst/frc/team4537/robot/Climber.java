package org.usfirst.frc.team4537.robot;

import edu.wpi.first.wpilibj.*;

public class Climber {
	private Robot robot;
	private final int CLIMBER = 0;
	private CANTalon winchTalon;
	private double speed = 1;
	
	public Climber(Robot robot) {
		this.robot = robot;
		winchTalon = new CANTalon(CLIMBER);
	}
	
	public void operatorControl() {
		if(robot.getController().raiseClimber()) {
			winchTalon.set(speed);
		}
		
		else if(robot.getController().lowerClimber()) {
			winchTalon.set(-speed);
		}
		
		else {
			winchTalon.set(0);
		}
	}
}
