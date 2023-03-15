package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.*;
import lib.RepeatingCommand;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BooleanSupplier;

public class Superstructure {
  public final Intake intake = new Intake();
  public final Shooter shooter = new Shooter();

//  private final Intake intake = new Intake();

// option a - David Arownowitz
//  public Command intakeBallsCommand() {
//    return new InstantCommand(
//          () -> intake.openClosePiston(true))
//          .andThen(
//                new RepeatingCommand(
//                      new ConditionalCommand(
//                            // deciding by whether the ball is our color
//                            new ConditionalCommand(
//                                  // whether the ultrasonic detects a ball
//                                  intake.closeIntakeCommand()
//                                        .andThen(new RunCommand(()-> {})),
//                                  intake.pullToUltrasonic(),
//                                  intake::isUltraDist),
//                            new ConditionalCommand(
//                                  // whether we want to eject from intake or from shooter
//                                  intake.ejectFromColorCommand(),
//                                  new RunCommand(() -> {
//                                    intake.setFrontMotorSpeed(0.3);
//                                    // TODO call the low shooter eject command
//                                  })
//                                        .until(() -> intake.isUltraDist())
//                                        .andThen(() -> intake.stopBackMotor())
//                                  /*TODO .andThen(stopShootMotorCommand*/,
//                                  intake::isUltraDist),
//                            intake::isOurColor)));
//  }

  // option b - Yoav Cohen
//    public Command intakeBallsCommand() {
//      AtomicBoolean intakeFull = new AtomicBoolean(false);
//    return new InstantCommand(
//          () -> intake.openClosePiston(true))
//          .andThen(
//                new RepeatingCommand(
//                      new ConditionalCommand(
//                            // deciding by whether the ball is our color
//                            new ConditionalCommand(
//                                  // whether the ultrasonic detects a ball
//                                  intake.closeIntakeCommand()
//                                        .andThen(new InstantCommand(()-> intakeFull.set(true))),
//                                  intake.pullToUltrasonic(),
//                                  intake::isUltraDist),
//                            new ConditionalCommand(
//                                  // whether we want to eject from intake or from shooter
//                                  intake.ejectFromColorCommand(),
//                                  new RunCommand(() -> {
//                                    intake.setFrontMotorSpeed(0.3);
//                                    // TODO call the low shooter eject command
//                                  })
//                                        .until(() -> intake.isUltraDist())
//                                        .andThen(() -> intake.stopBackMotor())
//                                  /*TODO .andThen(stopShootMotorCommand*/,
//                                  intake::isUltraDist),
//                            intake::isOurColor)).until(intakeFull::get));
//  }

  // option c
  public Command intakeBallsCommand() {
    return new RepeatingCommand(
          intake.pullToColorCommand()
                .andThen(
                      new ConditionalCommand(
                            // deciding by whether the ball is our color
                            new ConditionalCommand(
                                  // whether the ultrasonic detects a ball
                                  intake.closeIntakeCommand(),
                                  intake.pullToUltrasonicCommand(),
                                  intake.sonicTrigger),
                            new ConditionalCommand(
                                  // whether we want to eject from intake or from shooter
                                  intake.ejectFromColorCommand(),
                                  shootCommand(true),
                                  intake.sonicTrigger),
                            intake::isOurColor)))
          .until(() -> (intake.ballCount.get() == 2 && intake.isOurColor()));
  }

  public Command shootCommand() {
    return shootCommand(false);
  }

  public Command shootCommand(boolean lowShoot) {
    return shooter.accelerateShooterCommand(lowShoot)
          .alongWith(new RepeatingCommand(
                new WaitCommand(0.45)
                      .andThen(intake.pullToShooterCommand((() -> shooter.ballShotTrigger)
                      )))).until(()-> intake.ballCount.get() == 0);
  }
}
