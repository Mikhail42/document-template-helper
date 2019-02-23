
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.File
import java.sql.Date
import java.util.Calendar
import java.util.concurrent.atomic.AtomicInteger

import fr.opensagres.xdocreport.document.registry.XDocReportRegistry
import fr.opensagres.xdocreport.template.TemplateEngineKind

object Util {
  def addSlash(str: String): String = str.toCharArray.mkString("\u2006|\u2004")
  def addSlash(str: Int): String = addSlash(str.toString)
  def dd(cal: Calendar): String = "%02d".format(cal.get(Calendar.DAY_OF_MONTH))
  def ddWithSlash(cal: Calendar): String = dd(cal)
  def mm(cal: Calendar): String = "%02d".format(cal.get(Calendar.MONTH) + 1)
}
import Util._

case class PolysOMS(face: String, back: String)
case class Patient(fullname: String, birthday: Date, isMan: Boolean, registeredPlace: String, omsId: String, oms: PolysOMS) {
  val patientCalendar = Calendar.getInstance
  patientCalendar.setTime(birthday)
  def dd: String = Util.dd(patientCalendar)
  val mm: String = Util.mm(patientCalendar)
  def year: String = patientCalendar.get(Calendar.YEAR).toString
}

object Main extends App {
  def configToMap(conf: Iterator[String]): Map[String, String] = conf.map(e => e.split("=").map(_.trim)).map(ar => ar(0) -> ar(1)).toMap
  def loadAsMap(filename: String): Map[String, String] = configToMap(scala.io.Source.fromResource(filename).getLines)
  val id = new AtomicInteger(0)
  val patient = Patient("Ионкин Михаил Анатольевич", Date.valueOf("1994-03-12"), true, "Москва, ул. Фестивальная", "343324 35345434",
    PolysOMS("12345678901", "12345678901"))

  val nowCalendar = Calendar.getInstance
  nowCalendar.setTime(new Date(System.currentTimeMillis()))
  val dictionary = List("common.conf", "070u.conf").map(loadAsMap).reduce(_ ++ _) ++ Map(
    "p.fullname" -> patient.fullname,
    "p.xIfMan" -> (if (patient.isMan) "\u00d7" else ""),
    "p.xIfWoman" -> (if (!patient.isMan) "\u00d7" else ""),
    "p.registeredPlace" -> patient.registeredPlace,
    "p.oms.face" -> addSlash(patient.oms.face),
    "p.oms.back" -> addSlash(patient.oms.back),
  ) ++ Map(
    "dd" -> dd(nowCalendar),
    "month" -> mm(nowCalendar),
    "year" -> nowCalendar.get(Calendar.YEAR)
  ) + ("p" -> patient)

  val (folder, filename, ext) = ("/home/mikhail/Documents/", "forma_086-u_0", "docx")

  val registry = XDocReportRegistry.getRegistry
  def convert(folder: String, filename: String, ext: String) {
    import collection.JavaConverters._
    val inDoc = new FileInputStream(new File(s"${folder}${filename}.${ext}"))
    val report = registry.loadReport(inDoc, TemplateEngineKind.Velocity)
    val context = report.createContext()
    context.putMap(dictionary.mapValues(_.asInstanceOf[Object]).asJava)
    context.put("doc.id", id.incrementAndGet())
    val outDoc = new FileOutputStream(new File(s"${folder}${filename + "Out" + id.get()}.${ext}"))
    report.process(context, outDoc)
  }
  (0 until 3).foreach(_ => convert(folder, filename, ext))
  registry.dispose()
}
