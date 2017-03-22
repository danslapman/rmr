package rmr

trait This[T] {
  def `this`: T = new {}.asInstanceOf[T]
}

/**
  * [[http://api.mongodb.com/js/current/symbols/_global_.html]]
  */
object MongoApi {
  /**
    * @param b condition
    * @param msg message
    */
  def assert(b: Boolean, msg: String) {}

  /**
    * @param key result key
    * @param value result value
    */
  def emit(key: Any, value: Any) {}

  def gc() {}
  def isNumber(x: Any): Boolean = false
  def isObject(x: Any): Boolean = false
  def isString(x: Any): Boolean = false
  def print() {}

  /**
    * @param x value to print
    */
  def printjson(x: Any) {}

  /**
    * @param x value to print
    */
  def printjsononeline(x: Any) {}

  def tojson(x: Any, indent: Boolean, nolint: Boolean): String = ""
  def tojsononeline(x: Any): String = ""
  def tojsonObject(x: Any, indent: Boolean, nolint: Boolean): String = ""

  /**
    * [[http://api.mongodb.com/js/current/symbols/Array.html]]
    */
  object Array {
    def sum(values: Array[Int]): Int = 0
    def sum(values: Array[Double]): Double = 0
  }
}
