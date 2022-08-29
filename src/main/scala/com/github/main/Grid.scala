package com.github.main

import cats.data._
import cats.implicits._

/** Define an grid of arbitrary size
 *
 * @param sizeX The size of the x axis
 * @param sizeY The size of the y axis
 */
class Grid private (val sizeX: Int, val sizeY: Int)

object Grid {
  /** Validate the size for a grid axis.
   *
   * @param axis grid axis size
   * @param axisName the name of the axis (used for error messages)
   * @return ValidatedNel[Error, Int]
   */
  private def validateGridAxisSize(axis: Int, axisName: String): Validated[NonEmptyList[Error], Int] = {
    Validated.cond(
      axis > 0,
      axis,
      {
        val reason: String = if (axis < 0) "negative" else "zero"
        NonEmptyList(Error(s"Grid size for axis $axisName is $reason"), Nil)
      }
    )
  }

  /** Create a new Grid.
   *  Will return an error if either sizeX or sizeY are zero or negative
   *
   * @param sizeX Size of the X axis
   * @param sizeY Size of the Y Axis
   * @return new Grid of sizeX by sizeY
   */
  def apply(sizeX: Int, sizeY: Int): ValidatedNel[Error, Grid] = {
    (validateGridAxisSize(sizeX, "x"), validateGridAxisSize(sizeY, "y")).mapN { (x, y) =>
      new Grid(x, y)
    }
  }

  /** Create a Grid from a string in the format of x,y.
   *  Will return an error if either x or y is non-zero or negative
   *
   * @param xyString String in the format of x,y
   * @return Grid the size of x,y
   */
  def parseFromXYString(xyString: String): ValidatedNel[Error, Grid] = {
    def parse(value: String, prompt: String): ValidatedNel[Error, Int] = {
      value
        .toIntOption
        .toValidNel(Error(s"size of $prompt axis not a number"))
        .andThen(i => validateGridAxisSize(i, prompt))
    }

    val split = xyString.split(',')

    if (split.length != 2) {
      Error("Invalid grid size.").invalidNel[Grid]
    } else {
      (parse(split(0).trim, "x"), parse(split(1).trim, "y")).mapN(Grid.unsafe)
    }
  }

  /** Create a new Grid without checking the sizes.
   *  Only to be used for testing
   *
   * @param sizeX Size of the X axis
   * @param sizeY Size of the Y Axis
   * @return new Grid of sizeX by sizeY
   */
  def unsafe(sizeX: Int, sizeY: Int): Grid = {
    new Grid(sizeX, sizeY)
  }
}
