// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
    public static final class IntakeConstants {
        public static final int INTAKE_MOTOR_ID = 40; // PDP 4
        public static final int UPPER_MOTOR_ID = 41; // PDP 11
        public static final int UPPER_PING = 7;
        public static final int UPPER_ECHO = 6;
        public static final int COLOR_LIMIT = 55;
        public static final int SONIC_LIMIT = 220;
        public static final int MAX_BALLS = 2;
        public static final int FWD_CHANNEL = 3;
        public static final int REV_CHANNEL = 4;
        public static final double INTAKE_SPEED = 0.6;
        public static final double TRANSPORT_SPEED = 0.3;
      }
    
      public static class ClimberConstants {
        public static final int FORWARD_CHANNEL = 1;
        public static final int REVERSE_CHANNEL = 7;
        public static final int LEFT_MOTOR_ID = 30;
        public static final int RIGHT_MOTOR_ID = 31;

    
        public static final Value ANGLED = Value.kReverse;
        public static final Value STRAIGHT = Value.kForward;
      }
    
      public static class ShooterConstants {
        public static final int LEADER_ID = 20;
        public static final int FOLLOWER_ID = 21;
        public static final int ENCODER_A = 0;
        public static final int ENCODER_B = 1;
    
        // 121.45182291666667;
        public static final double FENDER_SHOT_RPM = 68;
        public static final double TOLERANCE = 4;
      }
    
      public static class DrivetrainConstants {
        public static final int LEFT_LEADER_ID = 10;
        public static final int RIGHT_LEADER_ID = 11;
        public static final int LEFT_FOLLOWER_ID = 12;
        public static final int RIGHT_FOLLOWER_ID = 13;
      }
    
      public static class LedsConstants {
        public static final int LEDS_PORT = 0;
      }
}
