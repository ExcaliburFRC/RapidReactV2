package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import com.revrobotics.RelativeEncoder;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

import java.util.function.BooleanSupplier;

public class Climber extends SubsystemBase {
  private final DoubleSolenoid piston =
        new DoubleSolenoid(PneumaticsModuleType.CTREPCM, Constants.ClimberConstants.FORWARD_CHANNEL,
              Constants.ClimberConstants.REVERSE_CHANNEL);
  private final CANSparkMax rightMotor =
        new CANSparkMax(Constants.ClimberConstants.RIGHT_MOTOR_ID, CANSparkMaxLowLevel.MotorType.kBrushless);
  private final CANSparkMax leftMotor =
        new CANSparkMax(Constants.ClimberConstants.LEFT_MOTOR_ID, CANSparkMaxLowLevel.MotorType.kBrushless);

  public Climber() {
    leftMotor.setInverted(true);
    rightMotor.setIdleMode(CANSparkMax.IdleMode.kBrake);
    leftMotor.setIdleMode(CANSparkMax.IdleMode.kBrake);
  }

  public Command manualCommand(
        BooleanSupplier leftUp, BooleanSupplier leftDown,
        BooleanSupplier rightUp, BooleanSupplier rightDown,
        BooleanSupplier openPiston, BooleanSupplier closePiston){
    return new RunCommand(
          ()-> {
            if (leftUp.getAsBoolean()) leftMotor.set(0.5);
            else if (leftDown.getAsBoolean()) leftMotor.set(-0.5);
            else leftMotor.set(0);
            if (rightUp.getAsBoolean()) rightMotor.set(0.5);
            else if (rightDown.getAsBoolean()) rightMotor.set(-0.5);
            else rightMotor.set(0);

            if (openPiston.getAsBoolean()) piston.set(DoubleSolenoid.Value.kReverse);
            if (closePiston.getAsBoolean()) piston.set(DoubleSolenoid.Value.kForward);
          }, this);
  }
}
