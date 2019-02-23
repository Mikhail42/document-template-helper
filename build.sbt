name := "document-template-helper"

version := "0.1"

scalaVersion := "2.12.8"

libraryDependencies ++= Seq(
  "fr.opensagres.xdocreport.core",
  "fr.opensagres.xdocreport.document",
  "fr.opensagres.xdocreport.document.odt",
  "fr.opensagres.xdocreport.document.docx",
  "fr.opensagres.xdocreport.template",
  "fr.opensagres.xdocreport.template.velocity",
  "fr.opensagres.xdocreport.template.freemarker",
  "fr.opensagres.xdocreport.itext.extension",
  "fr.opensagres.xdocreport.converter",
  "fr.opensagres.xdocreport.converter.odt.odfdom",
  "fr.opensagres.xdocreport.converter.docx.xwpf"
).map(name => "fr.opensagres.xdocreport" % name % "2.0.1")
//libraryDependencies += "fr.opensagres.xdocreport" % "org.odftoolkit.odfdom.converter.pdf" % "1.0.6"
//libraryDependencies += "fr.opensagres.xdocreport" % "fr.opensagres.xdocreport.converter.odt.odfdom " % "1.0.6"
// https://mvnrepository.com/artifact/com.typesafe/config
libraryDependencies += "com.typesafe" % "config" % "1.2.1"


