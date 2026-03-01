ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.5"

lazy val root = (project in file("."))
  .settings(
    name := "asmd-llm-code",
    libraryDependencies += "dev.langchain4j" % "langchain4j" % "1.11.0",
    libraryDependencies += "dev.langchain4j" % "langchain4j-ollama" % "1.11.0",
    libraryDependencies += "com.github.haifengl" %% "smile-scala" % "4.3.0",
    libraryDependencies += "com.github.haifengl" %% "smile-scala" % "4.3.0",
    libraryDependencies += "com.github.haifengl" % "smile-plot" % "4.3.0"
  )
