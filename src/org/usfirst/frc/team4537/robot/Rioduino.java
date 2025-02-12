package org.usfirst.frc.team4537.robot;

import edu.wpi.first.wpilibj.*;

public class Rioduino {
	
	public static final byte DISABLED = 1;
	public static final byte AUTONOMOUS = 2;
	public static final byte OPERATOR_CONTROLLED = 3;
	public static final byte TEST = 4;
	
	public static final byte CLIMBER_TRIGGERED = 5;
	public static final byte CLIMBER_WINDING = 6;
	public static final byte PHHHT = 7;
	public static final byte CLIMBER_UNWINDING = 8;
	public static final byte CLIMBER_DETRIGGERED = 9;
	public static final byte NO_ACTION = 99;
	
	private I2C i2c;
	private Robot robot;
	
	public Rioduino(Robot robot)
	{
		this.robot = robot;
		this.i2c = new I2C(I2C.Port.kMXP, 84);
	}

	public void send(byte b)
	{
		// Send the data across
		i2c.write(b, 0);
	}
}
