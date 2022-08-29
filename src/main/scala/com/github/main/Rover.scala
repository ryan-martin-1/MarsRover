package com.github.main

import cats.effect.{Ref, Sync}
import cats.syntax.all._
import com.github.main.Heading._

/** The Mars Rover
 *
 * @param grid The grid the Rover is on
 * @param position The rovers current position
 * @param heading The rovers current heading
 * @param sync$T$0 Monad for side effects
 * @tparam T Context
 */
case class Rover[T[_] : Sync](
  grid: Grid,
  private val position: Ref[T, Position],
  private val heading: Ref[T, Heading]
) {

  /** Get current rover position
   *
   * @return Rovers position
   */
  def getPosition: T[Position] = position.get

  /** Get current rover heading
   *
   * @return Rovers heading
   */
  def getHeading: T[Heading] = heading.get

  /** Given a heading and position, handle the movement of the rover
   *
   * @param heading current rover heading
   * @param position current rover position
   * @return The new position
   */
  private def handleMovement(heading: Heading, position: Position): Position = {
    def handleWrapping(pos: Int, base: Int, gridSize: Int): Int = {
      val value = ((pos + base) % gridSize)

      wrapIntValue(value, gridSize)
    }

    heading match {
      case UP | DOWN =>
        position.copy(y = handleWrapping(position.y, heading.baseMovementValue, grid.sizeY))
      case LEFT | RIGHT =>
        position.copy(x = handleWrapping(position.x, heading.baseMovementValue, grid.sizeX))
    }
  }

  /** Move the Rover based on current heading
   *
   * @return T[Unit]
   */
  def move(): T[Unit] = {
    for {
      heading <- heading.get
      _ <- position.update(pos => handleMovement(heading, pos))
    } yield ()
  }

  /** Rotate the Rover counter clockwise
   *
   * @return T[Unit]
   */
  def rotateClockwise(): T[Unit] = heading.update(heading => Heading.fromId(heading.id + 1))

  /** Rotate the Rover counter clockwise
   *
   * @return T[Unit]
   */
  def rotateCounterClockwise(): T[Unit] = heading.update(heading => Heading.fromId(heading.id - 1))
}

object Rover {
  def apply[T[_] : Sync](grid: Grid): T[Rover[T]] = {
    for {
      position <- Ref.of[T, Position](Position(0, 0))
      heading <- Ref.of[T, Heading](Heading.UP)
    } yield Rover[T](grid, position, heading)
  }
}
