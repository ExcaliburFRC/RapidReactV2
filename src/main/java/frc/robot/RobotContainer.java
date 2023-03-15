// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.PrintCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.button.Button;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.subsystems.*;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  private final Climber climber = new Climber();
  private final Drive drive = new Drive();
  private final Superstructure superstructure = new Superstructure();

//  private final PS4Controller controller = new PS4Controller(0);
  private final Joystick secondaryController = new Joystick(1);

  private final Compressor compressor = new Compressor(PneumaticsModuleType.CTREPCM);

  public RobotContainer() {
    // Configure the button bindings
    configureButtonBindings();
  }

  /**
   * Use this method to define your button->command mappings. Buttons can be created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a {@link
   * edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  public void configureButtonBindings() {
//    climber.setDefaultCommand(
//          climber.manualCommand(
//                () -> controller.getPOV() == 0,
//                () -> controller.getPOV() == 180,
//                () -> controller.getTriangleButton(),
//                () -> controller.getCrossButton(),
//                () -> controller.getPOV() == 90,
//                () -> controller.getPOV() == 270));

    drive.arcadeDriveCommand(()-> -secondaryController.getY(), secondaryController::getX).schedule();

    new Button(()-> CommandScheduler.getInstance().requiring(superstructure.intake) != null)
          .whenReleased(superstructure.intake.closeIntakeCommand());

    new Button(()-> secondaryController.getRawButton(1)).toggleWhenActive(superstructure.intakeBallsCommand());
    new Button(()-> secondaryController.getRawButton(3)).toggleWhenPressed(superstructure.intake.ejectBallsCommand());
    new Button(()-> secondaryController.getRawButton(2)).toggleWhenPressed(superstructure.shootCommand());
  }

  public void TestMode(){
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    return null;
  }
}
