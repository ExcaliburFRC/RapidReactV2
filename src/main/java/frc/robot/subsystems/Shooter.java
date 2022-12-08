package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.StartEndCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import static frc.robot.Constants.ShooterConstants.*;

public class Shooter extends SubsystemBase {
 private final CANSparkMax shooter = new CANSparkMax(LEADER_ID, CANSparkMaxLowLevel.MotorType.kBrushless);
 private final Encoder encoder = new Encoder(ENCODER_A, ENCODER_B);
 public boolean ballShotTrigger;
 double prevVel = 0;

 public Shooter(){
  shooter.setIdleMode(CANSparkMax.IdleMode.kBrake);
  shooter.setSmartCurrentLimit(CURRENT_LIMIT);
  shooter.setInverted(true);
  encoder.reset();
 }

 public Command accelerateShooterCommand(boolean lowShoot){
  return new StartEndCommand(
        ()-> {
         if (lowShoot) shooter.set(0.2);
         else shooter.set(0.35);
         },
        ()-> shooter.set(0), this);
 }

 double x = 0;
 double t = Timer.getFPGATimestamp();
 double velocity;

 private void updateVel(){
  double prev_x = x;
  double prev_t = t;

  x = encoder.getDistance();
  t = Timer.getFPGATimestamp();

  double dx = x - prev_x;
  double dt = t - prev_t;

  velocity = dx / dt;
 }

 @Override
 public void periodic() {
  updateVel();
  ballShotTrigger = prevVel > velocity + 500;
  prevVel = velocity;
 }
}
