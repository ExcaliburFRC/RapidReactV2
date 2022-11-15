package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import com.revrobotics.ColorSensorV3;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj2.command.*;
import frc.robot.Constants;

import java.util.function.BooleanSupplier;

public class Intake extends SubsystemBase {
  private int ballsQuantity;
  ;
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

  public Intake() {
    frontMotor.setIdleMode(CANSparkMax.IdleMode.kBrake);
    backMotor.setIdleMode(CANSparkMax.IdleMode.kBrake);

    frontMotor.setInverted(true);
    backMotor.setInverted(true);

    ultrasonic.setEnabled(true);
    ballsQuantity = 1;
  }

  private Command startEndIntake() {
    return new StartEndCommand(
          () -> {
            piston.set(DoubleSolenoid.Value.kForward);
            frontMotor.set(0.8);
          },
          () -> {
            piston.set(DoubleSolenoid.Value.kReverse);
            frontMotor.set(0);
          });
  }

  private Command backMotorsIntake() {
    return new StartEndCommand(() ->
          backMotor.set(0.8),
          () ->
                backMotor.set(0)
    );
  }

  public Command mainIntakeCommand() {
    return new ParallelCommandGroup(
          startEndIntake().until(() ->
                (colorSensor.getProximity() < Constants.IntakeConstants.COLOR_LIMIT &&
                      DriverStation.getAlliance().equals(DriverStation.Alliance.Red))
          )
    );
  }
}
