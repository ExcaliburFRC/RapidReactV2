package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import com.revrobotics.ColorSensorV3;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj2.command.*;
import frc.robot.Constants;

public class Intakee extends SubsystemBase {
  private int ballsQuantity;
  private final CANSparkMax frontMotor = new CANSparkMax(
        Constants.IntakeConstants.INTAKE_MOTOR_ID,
        CANSparkMaxLowLevel.MotorType.kBrushless);
  private final CANSparkMax backMotor = new CANSparkMax(
        Constants.IntakeConstants.UPPER_MOTOR_ID,
        CANSparkMaxLowLevel.MotorType.kBrushless);
  private final DoubleSolenoid piston = new DoubleSolenoid(
        PneumaticsModuleType.CTREPCM,
        Constants.IntakeConstants.FWD_CHANNEL,
        Constants.IntakeConstants.REV_CHANNEL);
  private final ColorSensorV3 colorSensor = new ColorSensorV3(I2C.Port.kMXP);
  private final Ultrasonic ultrasonic = new Ultrasonic(
        Constants.IntakeConstants.UPPER_PING,
        Constants.IntakeConstants.UPPER_ECHO);

  public Intakee() {
    frontMotor.setIdleMode(CANSparkMax.IdleMode.kBrake);
    backMotor.setIdleMode(CANSparkMax.IdleMode.kBrake);

    frontMotor.setInverted(true);
    backMotor.setInverted(true);

    ultrasonic.setEnabled(true);
    ballsQuantity = 1;
  }

  public void openClosePiston(boolean needToOpen) {
    if (needToOpen) piston.set(DoubleSolenoid.Value.kForward);
    else piston.set(DoubleSolenoid.Value.kReverse);
  }

  public void setFrontMotorSpeed(double speed) {
    frontMotor.set(speed);
  }

  public void setBackMotorSpeed(double speed) {
    backMotor.set(speed);
  }

  public double getColorDis() {
    return colorSensor.getProximity();
  }

  private double getUltrasonicDis() {
    return ultrasonic.getRangeMM();
  }

  public boolean isUltraDist() {
    return getColorDis() < Constants.IntakeConstants.SONIC_LIMIT;
  }

  public Command closeIntake() {
    return new InstantCommand(
          () -> openClosePiston(false), this)
          .andThen(new RunCommand(
                () -> setBackMotorSpeed(0)));
  }

  public Command pullToUltrasonic() {
    return new RunCommand(()-> setFrontMotorSpeed(0.5), this)
          .andThen(() -> setFrontMotorSpeed(0))
          .until(this::isUltraDist);
  }

  public boolean isOurColor() {
    switch (DriverStation.getAlliance()) {
      case Red:
        return colorSensor.getRed() > colorSensor.getBlue();
      case Blue:
        return colorSensor.getBlue() > colorSensor.getRed();
      default:
        DriverStation.reportError(" No alliance has been found", false);
        return false;
    }
  }
}
