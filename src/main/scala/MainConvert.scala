import java.io.{File, FileInputStream, FileOutputStream}

import fr.opensagres.xdocreport.converter.{ConverterRegistry, ConverterTypeTo, Options}
import fr.opensagres.xdocreport.core.document.DocumentKind

object MainConvert extends App {
  val folder = "/home/mikhail/Documents/"
  val filename = "forma_070-u"
  val ext = "docx"

  val options = Options.getFrom(DocumentKind.DOCX).to(ConverterTypeTo.PDF)
  val inDocx= new FileInputStream(new File(s"${folder}${filename}.${ext}"))
  val converter = ConverterRegistry.getRegistry().getConverter(options)
  val outPdf = new FileOutputStream(new File("ODTHelloWord2PDF.pdf"))
  converter.convert(inDocx, outPdf, options)
}
