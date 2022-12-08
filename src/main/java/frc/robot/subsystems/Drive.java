package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj2.command.*;
import frc.robot.Constants;

import java.util.function.DoubleSupplier;

public class Drive extends SubsystemBase {
  private final CANSparkMax right =
        new CANSparkMax(Constants.DrivetrainConstants.RIGHT_LEADER_ID, CANSparkMaxLowLevel.MotorType.kBrushless);
  private final CANSparkMax left =
        new CANSparkMax(Constants.DrivetrainConstants.LEFT_LEADER_ID, CANSparkMaxLowLevel.MotorType.kBrushless);
  private final CANSparkMax rightFollower =
        new CANSparkMax(Constants.DrivetrainConstants.RIGHT_FOLLOWER_ID, CANSparkMaxLowLevel.MotorType.kBrushless);
  private final CANSparkMax leftFollower =
        new CANSparkMax(Constants.DrivetrainConstants.LEFT_FOLLOWER_ID, CANSparkMaxLowLevel.MotorType.kBrushless);

  public Drive() {
    right.setSmartCurrentLimit(50);
    rightFollower.setSmartCurrentLimit(50);
    left.setSmartCurrentLimit(50);
    leftFollower.setSmartCurrentLimit(50);

    right.setInverted(true);
    left.setInverted(false);

    right.setIdleMode(CANSparkMax.IdleMode.kBrake);
    left.setIdleMode(CANSparkMax.IdleMode.kBrake);
    rightFollower.setIdleMode(CANSparkMax.IdleMode.kBrake);
    leftFollower.setIdleMode(CANSparkMax.IdleMode.kBrake);

    rightFollower.follow(right);
    leftFollower.follow(left);

  }

  private final DifferentialDrive drive = new DifferentialDrive(left, right);

  private double deadband(double deadband, double value) {
    return value < deadband && value > -deadband ? 0 : value;
  }
//
//  public Command arcadeDriveCommand(DoubleSupplier speed, DoubleSupplier rotation) {
//    return new RunCommand(() ->
//          drive.arcadeDrive(
//                deadband(0.25, speed.getAsDouble()),
//                deadband(0.25, rotation.getAsDouble())),
//          this);
//  }

  public Command arcadeDriveCommand(DoubleSupplier speed, DoubleSupplier rotation) {
    return new RunCommand(() ->
          drive.arcadeDrive(
                speed.getAsDouble(),
                rotation.getAsDouble()),
          this);
  }
  // manual arcade drive - couldn't properly tune

//  public Command arcadeDriveCommand(DoubleSupplier speed, DoubleSupplier rotation, double deadband) {
//    return new RunCommand(() -> {
//      double xSpeed = deadband(deadband, speed.getAsDouble());
//      double zRotation = deadband(deadband, rotation.getAsDouble()) * 0.5;
//
//      right.set(xSpeed - zRotation);
//      left.set(xSpeed + zRotation);
//    }, this);
//  }
}
