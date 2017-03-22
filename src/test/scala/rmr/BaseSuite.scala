package rmr

import org.scalatest.{BeforeAndAfter, Matchers}

abstract class BaseSuite extends org.scalatest.fixture.FunSuite with Matchers with BeforeAndAfter
