package com.github.main.test

import com.github.main.Heading._
import com.github.main._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers._

class PackageTests extends AnyFlatSpec {
  "wrapIntValue" should "wrap positive values that go over max" in {
    wrapIntValue(5, 4) shouldBe 1
  }
  it should "wrap negative values" in {
    wrapIntValue(-2, 4) shouldBe 2
    wrapIntValue(-5, 4) shouldBe 3
  }

  "Heading" should "handle values that are greater than 3" in {
    Heading.fromId(4) shouldBe UP
    Heading.fromId(17) shouldBe RIGHT
  }
}
