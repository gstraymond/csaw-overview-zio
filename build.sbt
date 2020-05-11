scalaVersion := "2.13.2"

name := "csaw-overview-zio"

libraryDependencies ++= Seq(
  "dev.zio" %% "zio"         % "1.0.0-RC18-2",
  "dev.zio" %% "zio-streams" % "1.0.0-RC18-2"
)
