package com.github.main.test

import cats.effect._
import cats.effect.testing.scalatest.AsyncIOSpec
import cats.syntax.all._
import com.github.main._

import org.scalatest.flatspec.AsyncFlatSpec
import org.scalatest.matchers.should.Matchers

class RoverTests extends AsyncFlatSpec with AsyncIOSpec with Matchers {
  val grid = Grid.unsafe(10, 10)

  def repeatN[T[_] : Sync](until: Int, thunk: => T[Unit]) = {
    for {
      counter <- Ref.of[T, Int](0)
      _ <- Sync[T].whileM_(counter.get.map(_ < until)) {
        thunk.flatMap(_ => counter.update(_ + 1))
      }
    } yield ()
  }

  "A Rover" should "handle clockwise rotation" in {
    val rotateTwice = for {
      rover <- Rover[IO](grid)
      _ <- rover.rotateClockwise()
      heading <- rover.getHeading
    } yield heading

    rotateTwice.asserting(_ shouldBe Heading.RIGHT)
  }
  it should "handle rotating clockwise further than 360 degrees" in {
    val rotateTwice = for {
      rover <- Rover[IO](grid)
      _ <- repeatN[IO](7, rover.rotateClockwise())
      heading <- rover.getHeading
    } yield heading

    rotateTwice.asserting(_ shouldBe Heading.LEFT)
  }
  it should "handle counter clockwise rotation" in {
    val rotateTwice = for {
      rover <- Rover[IO](grid)
      _ <- rover.rotateCounterClockwise()
      heading <- rover.getHeading
    } yield heading

    rotateTwice.asserting(_ shouldBe Heading.LEFT)
  }
  it should "handle rotating counter clockwise further than 360 degrees" in {
    val rotateTwice = for {
      rover <- Rover[IO](grid)
      _ <- repeatN[IO](7, rover.rotateCounterClockwise())
      heading <- rover.getHeading
    } yield heading

    rotateTwice.asserting(_ shouldBe Heading.RIGHT)
  }
  it should "move in the direction it is facing" in {
    // Move forward 5
    // Move right 2
    // Move left 6
    // Move down 1
    val move = for {
      rover <- Rover[IO](grid)

      _ <- repeatN[IO](5, rover.move())

      _ <- rover.rotateClockwise()
      _ <- repeatN[IO](2, rover.move())

      _ <- repeatN[IO](2, rover.rotateClockwise())
      _ <- repeatN[IO](6, rover.move())

      _ <- rover.rotateCounterClockwise()
      _ <- rover.move()

      position <- rover.getPosition
    } yield position

    move.asserting(_ shouldBe Position(6, 6))
  }
}
