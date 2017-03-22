package rmr

import org.jscala.JsAst
import reactivemongo.api.commands.{Command, CommandWithResult}
import reactivemongo.bson.{BSONDocument, BSONDocumentWriter}

case class InlineMapReduceCommand[TKey, T](
  collection: String,
  query: BSONDocument,
  map: JsAst,
  reduce: JsAst
) extends Command with CommandWithResult[InlineMapReduceCommandResult[TKey, T]]

object InlineMapReduceCommand {
  implicit def mapReduceCommandWriter[TKey, T]: BSONDocumentWriter[InlineMapReduceCommand[TKey, T]] =
    new BSONDocumentWriter[InlineMapReduceCommand[TKey, T]] {
    override def write(mrc: InlineMapReduceCommand[TKey, T]): BSONDocument = BSONDocument(
      "mapReduce" -> mrc.collection,
      "query" -> mrc.query,
      "map" -> mrc.map.asString,
      "reduce" -> mrc.reduce.asString,
      "out" -> BSONDocument("inline" -> 1)
    )
  }
}
