package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import com.revrobotics.ColorSensorV3;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj2.command.*;
import edu.wpi.first.wpilibj2.command.button.Trigger;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BooleanSupplier;

import static frc.robot.Constants.IntakeConstants.*;

public class Intake extends SubsystemBase {
  public final AtomicInteger ballCount = new AtomicInteger(0);
  private final CANSparkMax frontMotor = new CANSparkMax(
        INTAKE_MOTOR_ID,
        CANSparkMaxLowLevel.MotorType.kBrushless);
  private final CANSparkMax backMotor = new CANSparkMax(
        UPPER_MOTOR_ID,
        CANSparkMaxLowLevel.MotorType.kBrushless);
  private final DoubleSolenoid piston = new DoubleSolenoid(
        PneumaticsModuleType.CTREPCM,
        FWD_CHANNEL,
        REV_CHANNEL);
  private final ColorSensorV3 colorSensor = new ColorSensorV3(I2C.Port.kMXP);
  private final Ultrasonic ultrasonic = new Ultrasonic(
        UPPER_PING,
        UPPER_ECHO);

  public Trigger colorTrigger = new Trigger(() -> colorSensor.getProximity() > COLOR_LIMIT);
  public Trigger sonicTrigger = new Trigger(() -> ultrasonic.getRangeMM() < SONIC_LIMIT).debounce(0.2);



  public Intake() {
    frontMotor.setIdleMode(CANSparkMax.IdleMode.kBrake);
    backMotor.setIdleMode(CANSparkMax.IdleMode.kBrake);

    frontMotor.setInverted(true);
    backMotor.setInverted(true);

    ultrasonic.setEnabled(true);
    Ultrasonic.setAutomaticMode(true);
  }

  void openClosePiston(boolean needToOpen) {
    if (needToOpen) piston.set(DoubleSolenoid.Value.kForward);
    else piston.set(DoubleSolenoid.Value.kReverse);
  }

  public Command closeIntakeCommand() {
    return new InstantCommand(
          () -> openClosePiston(false), this)
          .andThen(stopFrontMotorCommand());
  }

  public Command pullToColorCommand() {
    return new FunctionalCommand(
          () -> openClosePiston(true),
          () -> frontMotor.set(0.3),
          (__) -> frontMotor.set(0),
          colorTrigger.debounce(0.1),
          this)
          .andThen(new InstantCommand(()-> ballCount.incrementAndGet()));
  }

  public Command pullToUltrasonicCommand() {
    return new StartEndCommand(
          ()-> {
            frontMotor.set(0.3);
            backMotor.set(0.1);
          },
          ()-> {
            frontMotor.set(0);
            backMotor.set(0);},
          this)
          .until(sonicTrigger);
  }

  public Command pullToShooterCommand(BooleanSupplier ballShotTrigger) {
    return new RunCommand(() -> backMotor.set(0.35), this)
          .until(ballShotTrigger)
          .andThen(new InstantCommand(()-> backMotor.set(0)))
          .andThen(new InstantCommand(()-> ballCount.decrementAndGet()));
  }

  public Command ejectFromColorCommand() {
    return new FunctionalCommand(
          () -> {},
          () -> frontMotor.set(-0.3),
          (__) -> {
            frontMotor.set(0);
            ballCount.decrementAndGet();
            },
          colorTrigger.negate().debounce(0.75),
          this);
  }

  public Command ejectFromUltrasonicCommand() {
    return new FunctionalCommand(
          () -> {},
          () -> backMotor.set(-0.3),
          (__) -> backMotor.set(0),
          colorTrigger,
          this);
  }

  public Command ejectBallsCommand() {
    return new InstantCommand(()-> openClosePiston(true), this)
    .andThen(
          new ConditionalCommand(
          new ConditionalCommand(
                      ejectFromColorCommand(),
                new InstantCommand(()-> {}),
                colorTrigger)
                .andThen(ejectFromUltrasonicCommand().andThen(ejectFromColorCommand())),
          new InstantCommand(() -> {}),
          sonicTrigger), new InstantCommand(()-> openClosePiston(false)))
          .andThen(new InstantCommand(()-> ballCount.set(0)));
  }

  public boolean isOurColor() {
    switch (DriverStation.getAlliance()) {
      case Red:
        return colorSensor.getRed() > colorSensor.getBlue();
      case Blue:
        return colorSensor.getBlue() > colorSensor.getRed();
      default:
        DriverStation.reportError("No alliance has been found", false);
        return false;
    }
  }

  @Override
  public void initSendable(SendableBuilder builder) {
    builder.addBooleanProperty("sonic trigger: ", ()-> sonicTrigger.get(), null);
    builder.addBooleanProperty("color trigger: ", ()-> colorTrigger.get(), null);
    builder.addDoubleProperty("ballCount", ()-> ballCount.get(), null);
  }

  public Command setFrontMotorCommand(double speed) {
    return new RunCommand(() -> frontMotor.set(speed), this);
  }

  public Command stopFrontMotorCommand() {
    return new InstantCommand(() -> frontMotor.set(0), this);
  }

  public Command stopBackMotorCommand() {
    return new InstantCommand(() -> backMotor.set(0), this);
  }
}
