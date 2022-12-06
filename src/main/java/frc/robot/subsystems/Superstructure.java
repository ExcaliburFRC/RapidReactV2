package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.*;
import lib.RepeatingCommand;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BooleanSupplier;

public class Superstructure {
  private final Intake intake = new Intake();
  private final Shooter shooter = new Shooter();

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
//                                  intake.closeIntake()
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
//                                  intake.closeIntake()
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
//    public Command intakeBallsCommand() {
//    return new InstantCommand(
//          () -> intake.openClosePiston(true))
//          .andThen(
//                new RepeatingCommand(
//                      new ConditionalCommand(
//                            // deciding by whether the ball is our color
//                            new ConditionalCommand(
//                                  // whether the ultrasonic detects a ball
//                                  intake.closeIntake()
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
//                                  /* TODO .andThen(stopShootMotorCommand*/,
//                                  intake::isUltraDist),
//                            intake::isOurColor))
//                      .until(intake::intakeFull));
//  }
}
