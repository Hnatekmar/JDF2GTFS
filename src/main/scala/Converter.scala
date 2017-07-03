
import java.io.File
import javax.annotation.processing.FilerException
import java.nio.file.{Files, Paths}

import org.h2.jdbc.JdbcSQLException
import scalikejdbc._

/**
  * Hlavní třída převodníku:
  * Stará se především o rozbalení
  * @author Martin Hnátek
  */
class Converter {
  Class.forName("org.h2.Driver")
  GlobalSettings.loggingSQLAndTime = LoggingSQLAndTimeSettings(
    enabled = false,
    singleLineMode = false,
    printUnprocessedStackTrace = false,
    stackTraceDepth= 15,
    logLevel = 'debug,
    warningEnabled = false,
    warningThresholdMillis = 3000L,
    warningLogLevel = 'warn
  )
  ConnectionPool.singleton("jdbc:h2:mem:transfer;DB_CLOSE_DELAY=-1", "user", "pass")
  private implicit val session = AutoSession

  /**
    * Vrátí pole stringů obsahující názvy souborů, které se nachází v adresáři
    *
    * @param dir Název adresáře
    * @return Pole stringů
    */
  private def getListOfFiles(dir: File): Array[String] = dir.listFiles().map(x => x.toString)

  /**
    * Rekurzivně odstraní všechny soubory v adresáři.
    *
    * @param file adresář
    */
  private def deleteFolder(file: File): Unit = {
    if (file.isDirectory) {
      file.listFiles().foreach(deleteFolder)
      file.delete
    }
    else if (file.isFile && !file.delete()) {
      throw new FilerException("Soubor jmenem " + file.getName + " nelze odstranit!")
    }
  }

  /**
    * Vytvoří adresář s daným názvem
    *
    * @param name název adresáře
    * @return true pokud se operace podařila jinak false
    */
  private def createDir(name: String): Boolean = {
    new File(name).mkdir()
  }

  /**
    * Provede samotný převod
    * @param pathToGTFS cesta pro extrakci GTFS
    * @param pathToJDF cesta ke složce s tabulkami JDF
    */
  private def convertJDFToGTFS(pathToGTFS: String)(pathToJDF: String) = {
    if (Files.exists(Paths.get(pathToGTFS))) {
      deleteFolder(new File(pathToGTFS))
    }
    createDir("tmp/jdf")
    new Unzipper(pathToJDF).unzip("./tmp/jdf/")
    try {
      val jdf = JDFDirector("./tmp/jdf/")
    }
    catch
    {
      case ex:JdbcSQLException => println(s"Data z $pathToJDF se nepodařilo načíst. Pravděpodobně se jedná o chybu v CSV")
    }
    finally {
      deleteFolder(new File("tmp/jdf"))
      deleteFolder(new File(pathToJDF))
    }
  }

  /**
    * @param pathToZip Cesta k zipu ve kterém se nachází JDF
    * @param pathToGTFS Cesta ve které lze vytvořit GTFS soubory
    */
  def convert(pathToZip: String, pathToGTFS: String): Unit = {
    if(Files.exists(Paths.get("./tmp"))) {
      deleteFolder(new File("./tmp"))
    }
    createDir("tmp")
    if(Files.exists(Paths.get(pathToZip))) {
      new Unzipper(pathToZip).unzip("./tmp/")
      getListOfFiles(new File("tmp")).foreach(convertJDFToGTFS(pathToGTFS))
      deleteFolder(new File("./tmp/"))
    }
    else {
      Console.err.println("Nelze nálezt zip s JDF!")
    }
  }
}
