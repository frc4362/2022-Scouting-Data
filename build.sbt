name := "scoutingapp2019"

version := "0.1.0"

scalaVersion := "2.12.8"
scalaSource in Compile := baseDirectory.value / "src" / "main" / "scala"

val startingClass = Some("com.gemsrobotics.scouting2019.DataScoutingApp")

exportJars := true
retrieveManaged := true
mainClass in (Compile, run) := startingClass
mainClass in assembly := startingClass

enablePlugins(JavaFxPlugin)

javaFxMainClass := "com.gemsrobotics.scouting2022.DataScoutingApp"

fork in run := true

fullClasspath in assembly := (fullClasspath in Compile).value
assemblyJarName in assembly := s"gemscout4v${version.value}.jar"

libraryDependencies ++= Seq(
	"org.scalafx" %% "scalafx" % "11-R16",
	"com.github.tototoshi" %% "scala-csv" % "1.3.5"
)

lazy val osName = System.getProperty("os.name") match {
	case n if n.startsWith("Linux")   => "linux"
	case n if n.startsWith("Mac")     => "mac"
	case n if n.startsWith("Windows") => "win"
	case _ => throw new Exception("Unknown platform!")
}

lazy val javaFXModules = Seq("base", "controls", "fxml", "graphics", "media", "swing", "web")
libraryDependencies ++= javaFXModules.map { m =>
	"org.openjfx" % s"javafx-$m" % "11" classifier osName
}

assemblyMergeStrategy in assembly := {
	case PathList("META-INF", _ @ _*) => MergeStrategy.discard
	case _ => MergeStrategy.first
}