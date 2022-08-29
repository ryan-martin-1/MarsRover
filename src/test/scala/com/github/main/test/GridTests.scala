package com.github.main.test

import com.github.main.Grid
import org.scalatest.EitherValues
import org.scalatest.flatspec.AnyFlatSpec

class GridTests extends AnyFlatSpec with EitherValues {
  "A Grid" should "not accept a negative x value" in {
    val grid = Grid(-1, 1)

    assert(grid.isInvalid)
    assert(grid.toEither.left.value.length == 1)
  }
  it should "not accept a zero x value" in {
    val grid = Grid(0, 1)

    assert(grid.isInvalid)
    assert(grid.toEither.left.value.length == 1)
  }
  it should "not accept a negative y value" in {
    val grid = Grid(1, -1)

    assert(grid.isInvalid)
    assert(grid.toEither.left.value.length == 1)
  }
  it should "not accept a zero y value" in {
    val grid = Grid(1, 0)

    assert(grid.isInvalid)
    assert(grid.toEither.left.value.length == 1)
  }
  it should "report multiple errors" in {
    val grid = Grid(0, 0)

    assert(grid.isInvalid)
    assert(grid.toEither.left.value.length == 2)
  }
}
