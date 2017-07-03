
/**
  * Created by martin on 3.7.17.
  */
object JDFDirector {
  def apply(path: String): JDF = {
    val jdfVersionReader = scala.io.Source.fromFile(path + "VerzeJDF.txt").getLines().map(_.split(",")).toList
    val version = jdfVersionReader.head.head.replaceAll("[\";]", "")
    version match {
      case "1.11" => new JDF1_11(path)
      case _ => null
    }
  }
}
