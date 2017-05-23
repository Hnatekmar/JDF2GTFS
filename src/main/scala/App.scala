import scalikejdbc._


object App {
  // Inicializace databáze
  Class.forName("org.h2.Driver")
  ConnectionPool.singleton("jdbc:h2:mem:db1", "user", "pass")
  implicit val session = AutoSession

  def main(args: Array[String]): Unit = {
  }
}
