package com.github

package object main {
  /** Defines possible movements for the Mars Rover */
  object Movement extends Enumeration {
    type Movement = Value
    val FORWARD, ROTATE_CW, ROTATE_CCW = Value
  }

  /** Given a value and a maximum, wrap that integer value so that it
   *  is always in the bounds of 0 to max
   *
   * @param value to wrap
   * @param max value to not exceed
   * @return the value wrapped
   */
  def wrapIntValue(value: Int, max: Int): Int = {
    val base = value % max
    if (base < 0)
      max + base
    else
      base
  }

  /** Defines the heading in a 2D grid */
  trait Heading {
    /** Defines how a heading moves in a 2D grid */
    val baseMovementValue: Int

    /** Integer Id */
    val id: Int
  }

  object Heading {
    private val headingsReference: Array[Heading] = Array[Heading](UP, RIGHT, DOWN, LEFT)

    /** Get a Heading from a Integer id
     *  Acts like a compass where 0 is Up, 1 is Right, 2 is Left and 3 is Down
     *  This function ensures the id value is always between 0 and 3
     *
     * @param id to convert to Heading
     * @return Heading from Integer id
     */
    def fromId(id: Int): Heading = {
      headingsReference(wrapIntValue(id, headingsReference.length))
    }

    case object UP extends Heading {
      override val baseMovementValue: Int = -1
      override val id: Int = 0

      override def toString: String = "Up"
    }

    case object DOWN extends Heading {
      override val baseMovementValue: Int = 1
      override val id: Int = 2

      override def toString: String = "Down"
    }

    case object LEFT extends Heading {
      override val baseMovementValue: Int = -1
      override val id: Int = 3

      override def toString: String = "Left"
    }

    case object RIGHT extends Heading {
      override val baseMovementValue: Int = 1
      override val id: Int = 1

      override def toString: String = "Right"
    }
  }
}
