package rmr

import reactivemongo.bson.Macros.Annotations.Key
import reactivemongo.bson.{BSONDocument, BSONDocumentReader, BSONReader, BSONValue, Macros}

case class InlineMapReduceCommandResultEntry[TKey, T](@Key("_id") id: TKey, value: T)

object InlineMapReduceCommandResultEntry {
  implicit def reader[TKey, TKeyBV <: BSONValue, T, TBV <: BSONValue](implicit
    keyHandler: BSONReader[TKeyBV, TKey],
    valueHandler: BSONReader[TBV, T]
  ): BSONDocumentReader[InlineMapReduceCommandResultEntry[TKey, T]] =
    Macros.reader[InlineMapReduceCommandResultEntry[TKey, T]]
}

case class InlineMapReduceCommandCounts(input: Int, emit: Int, reduce: Int, output: Int)

object InlineMapReduceCommandCounts {
  implicit val reader: BSONDocumentReader[InlineMapReduceCommandCounts] =
    Macros.reader[InlineMapReduceCommandCounts]
}

case class InlineMapReduceCommandResult[TKey, T](
  results: Vector[InlineMapReduceCommandResultEntry[TKey, T]],
  timeMillis: Int,
  counts: InlineMapReduceCommandCounts,
  ok: Double
)

object InlineMapReduceCommandResult {
  implicit def reader[TKey, T](implicit
    entryHandler: BSONDocumentReader[InlineMapReduceCommandResultEntry[TKey, T]]
  ): BSONDocumentReader[InlineMapReduceCommandResult[TKey, T]] = new BSONDocumentReader[InlineMapReduceCommandResult[TKey, T]] {
    override def read(bson: BSONDocument): InlineMapReduceCommandResult[TKey, T] =
      (for {
        results <- bson.getAsTry[Vector[InlineMapReduceCommandResultEntry[TKey, T]]]("results")
        timeMillis <- bson.getAsTry[Int]("timeMillis")
        counts <- bson.getAsTry[InlineMapReduceCommandCounts]("counts")
        ok <- bson.getAsTry[Double]("ok")
      } yield InlineMapReduceCommandResult(results, timeMillis, counts, ok)).get
  }
}