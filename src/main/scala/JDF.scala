import scalikejdbc._

/**
  * Obaluje operace nad databází JDF
  *
  * @author Martin Hnátek
  */
abstract class JDF(jdfFolder: String, val tableColumns: List[String], val columnNames: List[String])(implicit session: DBSession = AutoSession) {
  val tables = List("Caskody", "Dopravci", "Linky", "Spoje", "Zaslinky", "Zasspoje",
    "Zastavky")

  tables.zip(tableColumns).zip(columnNames).map({ case ((x, y), z) => (x, y, z) }).foreach(tableInfo => {
    val (name, columns, names) = tableInfo
    val nullIfs = names.split(",")
      .map(x => "NULLIF(\"" + x + "\", '')")
      .reduceLeft((x, y) => x + ", " + y)

    SQL(s"CREATE TABLE IF NOT EXISTS $name($columns)").execute.apply()
    SQL(s"INSERT INTO $name (SELECT $nullIfs FROM CSVREAD('./tmp/jdf/$name.txt', '$names', 'charset=Windows-1250 lineComment=\\;'))").execute.apply()
  })
}
