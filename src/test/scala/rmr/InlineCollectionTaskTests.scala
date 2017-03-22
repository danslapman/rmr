package rmr
import org.scalatest.Outcome
import reactivemongo.api.collections.bson._
import reactivemongo.api._
import reactivemongo.bson.{BSONDocument, Macros}

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

case class Item(custId: String, amount: Int, status: String)

object ThisItem extends This[Item]

class InlineCollectionTaskTests extends BaseSuite {
  private val collectionName = "inline"

  private val driver = new MongoDriver
  private val addresses: List[String] = List("localhost:27017")
  private val options = MongoConnectionOptions(connectTimeoutMS = 10000)
  private val connection = driver.connection(addresses, options = options)
  private val db: DefaultDB = Await.result(connection.database("rmr"), 1.second)

  override type FixtureParam = DefaultDB

  private implicit val itemHandler = Macros.handler[Item]

  private def seed(collection: BSONCollection) = {
    val data = Item("A123", 500, "A") :: Item("A123", 250, "A") ::
      Item("B212", 200, "A") :: Item("A123", 300, "D") :: Nil

    Await.ready(
      collection.bulkInsert(ordered = false)(data.map(implicitly[collection.ImplicitlyDocumentProducer](_)): _*),
      30.seconds)
  }

  override protected def withFixture(test: OneArgTest): Outcome = {
    try {
      seed(db.collection[BSONCollection](collectionName))
      withFixture(test.toNoArgTest(db))
    } finally {
      Await.ready(db.collection[BSONCollection](collectionName).drop(failIfNotFound = false), 30.seconds)
    }
  }

  test("inline MapReduce with query") { db =>
    import org.jscala._
    import MongoApi._
    import ThisItem._

    val mapper = javascript {
      () => { emit(`this`.custId, `this`.amount) }
    }

    val reducer = javascript {
      (key: String, values: Array[Int]) => { Array.sum(values) }
    }

    val result = Await.result(
      db.runCommand(
        InlineMapReduceCommand[String, Double](collectionName, BSONDocument("status" -> "A"), mapper, reducer), FailoverStrategy.default),
      10.seconds)

    result.results shouldBe Vector(
      InlineMapReduceCommandResultEntry("A123", 750),
      InlineMapReduceCommandResultEntry("B212", 200)
    )
  }
}
