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
       DatumOd BIGINT,
       DatumDo BIGINT,
       Poznamka VARCHAR(254),
       RozliseniLinky BIGINT
    """,
    """
      IC VARCHAR(10),
      DIC VARCHAR(14),
      ObchodniJmeno VARCHAR(254),
      DruhFirmy BIGINT,
      JmenoFyzickeOsoby VARCHAR(254),
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
       PlatnostLicenceOd BIGINT,
       PlatnostLicenceDo BIGINT,
       PlatnostJROd BIGINT,
       PlatnostJRDo BIGINT,
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
       CasPrijezdu VARCHAR(5),
       CasOdjezdu VARCHAR(5),
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
  val columnNames = List(
    "CisloLinky,CisloSpoje,CisloCasovehoKodu,OznaceniCasovehoKodu,TypCasovehoKodu,DatumOd,DatumDo,Poznamka,RozliseniLinky",
    "IC,DIC,ObchodniJmeno,DruhFirmy,JmenoFyzickeOsoby,Sidlo,TelefonSidla,TelefonDispecink,TelefonInformace,Fax,EMail,WWW,RosliseniDopravce",
    "CisloLinky,NazevLinky,ICDopravce,TypLinky,DopravniProstredek,ObjizdkovyJR,SeskupeniSpoju,PouzitiOznacniku,Rezerva,CisloLicence,PlatnostLicenceOd,PlatnostLicenceDo,PlatnostJROd,PlatnostJRDo,RosliseniDopravce,RosliseniLinky",
    "CisloLinky,CisloSpoje,PevKod1,PevKod2,PevKod3,PevKod4,PevKod5,PevKod6,PevKod7,PevKod8,PevKod9,PevKod10,KodSkupinySpoju,RozliseniLinky",
    "CisloLinky,CisloTarifni,TarifniPasmo,CisloZastavky,PrumernaDoba,PevKod1,PevKod2,PevKod3,RozliseniLinky",
    "CisloLinky,CisloSpoje,CisloTarifni,CisloZastavky,KodOznacniku,CisloStanoviste,PevKod1,PevKod2,Kilometry,CasPrijezdu,CasOdjezdu,RozliseniLinky",
    "CisloZastavky,NazevObce,CastObce,BlizsiMisto,BlizkaObec,Stat,PevKod1,PevKod2,PevKod3,PevKod4,PevKod5,PevKod6"
  )
  //tables.foreach(tableName => SQL(s"DROP TABLE IF EXISTS $tableName").execute.apply())
  tables.zip(tableColumns).zip(columnNames).map({case ((x, y), z) => (x, y, z)}).foreach(tableInfo =>
  {
    val (name, columns, names) = tableInfo
    val nullIfs = names.split(",")
      .map(x => "NULLIF(\"" + x + "\", '')")
      .reduceLeft((x, y) => x + ", " + y)

    SQL(s"CREATE TABLE IF NOT EXISTS $name($columns)").execute.apply()
    SQL(s"INSERT INTO $name (SELECT $nullIfs FROM CSVREAD('./tmp/jdf/$name.txt', '$names', 'charset=Windows-1250 lineComment=\\;'))").execute.apply()
  })
}
