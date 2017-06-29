import scalikejdbc._

/**
  * Obaluje operace nad databází JDF
  *
  * @author Martin Hnátek
  */
class JDF(jdfFolder: String)(implicit session: DBSession = AutoSession) {
  val tables = List("Caskody", "Dopravci", "Linky", "Spoje", "Zaslinky", "Zasspoje",
    "Zastavky")
  val tableColumns = List(
    """
       CisloLinky BIGINT,
       CisloSpoje BIGINT,
       CisloCasovehoKodu BIGINT,
       OznaceniCasovehoKodu VARCHAR(2),
       TypCasovehoKodu VARCHAR(1),
       DatumOd DATETIME,
       Poznamka VARCHAR(254),
       RozliseniLinky BIGINT
    """,
    """
      IC VARCHAR(10),
      DIC VARCHAR(14),
      ObchodniJmeno VARCHAR(254),
      DruhFirmy BIGINT,
      Sidlo VARCHAR(254),
      TelefonSidla VARCHAR(48),
      TelefonDispecink VARCHAR(48),
      TelefonInformace VARCHAR(48),
      Fax VARCHAR(48),
      EMail VARCHAR(48),
      WWW VARCHAR(48),
      RosliseniDopravce BIGINT
    """,
    """
       CisloLinky BIGINT,
       NazevLinky VARCHAR(254),
       ICDopravce VARCHAR(10),
       TypLinky VARCHAR(1),
       DopravniProstredek VARCHAR(1),
       ObjizdkovyJR CHAR,
       SeskupeniSpoju CHAR,
       PouzitiOznacniku CHAR,
       Rezerva VARCHAR(5),
       CisloLicence VARCHAR(48),
       PlatnostLicenceOd DATETIME,
       PlatnostLicenceDo DATETIME,
       PlatnostJROd DATETIME,
       PlatnostJRDo DATETIME,
       RosliseniDopravce BIGINT,
       RosliseniLinky BIGINT
    """,
    """
       CisloLinky BIGINT,
       CisloSpoje BIGINT,
       PevKod1 VARCHAR(5),
       PevKod2 VARCHAR(5),
       PevKod3 VARCHAR(5),
       PevKod4 VARCHAR(5),
       PevKod5 VARCHAR(5),
       PevKod6 VARCHAR(5),
       PevKod7 VARCHAR(5),
       PevKod8 VARCHAR(5),
       PevKod9 VARCHAR(5),
       PevKod10 VARCHAR(5),
       KodSkupinySpoju BIGINT,
       RozliseniLinky BIGINT
    """,
    """
       CisloLinky BIGINT,
       CisloTarifni BIGINT,
       TarifniPasmo VARCHAR(50),
       CisloZastavky BIGINT,
       PrumernaDoba VARCHAR(5),
       PevKod1 VARCHAR(5),
       PevKod2 VARCHAR(5),
       PevKod3 VARCHAR(5),
       RozliseniLinky BIGINT
    """,
    """
       CisloLinky BIGINT,
       CisloSpoje BIGINT,
       CisloTarifni BIGINT,
       CisloZastavky BIGINT,
       KodOznacniku BIGINT,
       CisloStanoviste VARCHAR(48),
       PevKod1 VARCHAR(5),
       PevKod2 VARCHAR(5),
       Kilometry INT,
       CasPrijezdu DATETIME,
       CasOdjezdu DATETIME,
       RozliseniLinky BIGINT
    """,
    """
       CisloZastavky BIGINT,
       NazevObce VARCHAR(48),
       CastObce VARCHAR(48),
       BlizsiMisto VARCHAR(48),
       BlizkaObec VARCHAR(3),
       Stat VARCHAR(3),
       PevKod1 VARCHAR(5),
       PevKod2 VARCHAR(5),
       PevKod3 VARCHAR(5),
       PevKod4 VARCHAR(5),
       PevKod5 VARCHAR(5),
       PevKod6 VARCHAR(5)
    """
  )
  tables.foreach(tableName => SQL(s"DROP TABLE IF EXISTS $tableName").execute.apply())
  tables.zip(tableColumns).foreach(tableInfo => {
    val (name, columns) = tableInfo
    SQL(s"CREATE TABLE IF NOT EXISTS $name($columns)").execute.apply()
  })
}
