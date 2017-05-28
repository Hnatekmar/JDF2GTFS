import java.io.{BufferedOutputStream, FileOutputStream}
import java.util.zip._

/**
  * Generická třída pro rozbalování soborů typu zip.
  * @param name název souboru
  * @author Martin Hnátek
  */
class Unzipper(name: String) {

  private val zipFile = new ZipFile(name)

  /**
    * Rozbalí obsah zipu do specifikované cesty
    * @param path cesta do které se má zip rozbalit
    */
  def unzip(path: String): Unit = {
    val files = zipFile.entries()
    while(files.hasMoreElements) {
      val zipData = files.nextElement()
      val data: Array[Byte] = new Array[Byte](1024)
      val stream = zipFile.getInputStream(zipData)
      val output = new BufferedOutputStream(new FileOutputStream(path + zipData.getName))
      Stream
        .continually(stream.read(data))
        .takeWhile(-1 !=)
        .foreach(read => output.write(data, 0, read))
      output.close()
    }
  }
}
