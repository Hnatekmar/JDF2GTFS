/**
  * Hlavní třída aplikace
  * @author Martin Hnátek
  */
object App {
  def main(args: Array[String]): Unit = {
    val converter = new Converter()
    converter.convert("./JDF.zip", "GTFS")
  }
}
