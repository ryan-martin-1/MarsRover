package com.github.main

import cats.data.Validated.{Invalid, Valid}
import cats.effect._
import cats.syntax.all._

import scala.io.StdIn.readLine

object Main extends IOApp {
  private def processCommand[T[_] : Sync](command: String, rover: Rover[T]): T[Boolean] = {
    val cleaned = command.toLowerCase().strip()

    val processCommand: T[Unit] = cleaned match {
      case "move" => rover.move()
      case "cw" => rover.rotateClockwise()
      case "ccw" => rover.rotateCounterClockwise()
      case _ => Sync[T].unit
    }

    processCommand.map(_ => cleaned == "exit")
  }

  def loop[T[_] : Sync](shouldExit: Ref[T, Boolean], rover: Rover[T]): T[Unit] = {
    Sync[T].whileM_(shouldExit.get.map(!_)) {
      for {
        pos <- rover.getPosition
        head <- rover.getHeading
        _ <- Sync[T].blocking {
          println(s"Grid size: ${rover.grid.sizeX}, ${rover.grid.sizeY}")
          println(s"Rover heading: $head")
          println(s"Rover position: ${pos.x}, ${pos.y}")
          println(s"Please enter the following commands:")
          println(s"move\tto move rover in the direction of the heading value")
          println(s"cw\t\trotate clockwise")
          println(s"ccw\t\trotate counter clockwise")
          println(s"exit\tto exit")
        }
        in <- Sync[T].blocking(readLine())
        result <- processCommand(in, rover)
        _ <- shouldExit.update(_ => result)
        _ <- Sync[T].blocking(println(in))
      } yield ()
    }
  }

  def init[T[_]: Sync](): T[Rover[T]] = {
    for {
      _ <- Sync[T].blocking {
        print("\u001b[2J") // Clear screen
        println("Enter grid size as x,y")
      }
      size <- Sync[T].blocking(readLine())
      grid = Grid.parseFromXYString(size)
      rover <- grid match {
        case Valid(grid) =>
          Rover[T](grid)
        case Invalid(errs) =>
          Sync[T]
            .blocking {
              errs.toList.foreach(err => println(err.message))
              println("Press Enter to retry.")
            }
            .flatMap(_ => Sync[T].blocking(readLine()))
            .flatMap(_ => init[T]())
      }
    } yield rover
  }

  override def run(args: List[String]): IO[ExitCode] = {
    for {
      rover <- init[IO]()
      shouldExit <- Ref.of[IO, Boolean](false)
      _ <- loop[IO](shouldExit, rover)
    } yield ExitCode.Success
  }
}
