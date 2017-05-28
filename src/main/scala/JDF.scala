import scalikejdbc._

/**
  * Obaluje operace nad databází JDF
  *
  * @author Martin Hnátek
  */
class JDF(jdfFolder: String)(implicit session: DBSession = AutoSession) {
  val tables = List("Caskody", "Dopravci", "Linky", "Spoje", "Zaslinky", "Zasspoje",
    "Zastavky")
  tables.foreach(tableName => SQL(s"DROP TABLE IF EXISTS $tableName").execute.apply())
  tables.foreach(tableName => SQL(s"CREATE TABLE $tableName AS SELECT * FROM CSVREAD('$jdfFolder$tableName.txt')")
    .execute.apply())
}
