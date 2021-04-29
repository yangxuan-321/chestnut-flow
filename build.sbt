ThisBuild / name := "chestnut-flow"
ThisBuild / organization := "org.moda"
ThisBuild / scalaVersion := "2.13.1"

lazy val root = project
  .in(file("."))
  .settings(name := (name in ThisBuild).value)
  .settings(version := (version in ThisBuild).value)
  .settings(coverageSettings)
  .enablePlugins(JavaAppPackaging)
  .enablePlugins(UniversalPlugin)
  .aggregate(core)

lazy val idl = project
  .in(file("idl"))
  .settings(name := "chestnut-idl")
  .settings(version := (version in ThisBuild).value)
  .settings(scalacOptions ++= commonScalacOptions)
  .settings(Seq(jettyAlpnAgent, scalapbDeps))
  .enablePlugins(AkkaGrpcPlugin)
  .enablePlugins(JavaAgent)
  .enablePlugins(JavaAppPackaging)
  .enablePlugins(UniversalPlugin)
  .settings(depOverrides)

lazy val common = project
  .in(file("common"))
  .dependsOn(idl)
  .settings(name := "chestnut-common")
  .settings(version := (version in ThisBuild).value)
  .settings(scalacOptions ++= commonScalacOptions)
  .settings(Seq(jettyAlpnAgent, scalapbDeps))
  .enablePlugins(AkkaGrpcPlugin)
  .enablePlugins(JavaAgent)
  .enablePlugins(JavaAppPackaging)
  .enablePlugins(UniversalPlugin)
  .settings(commonDeps, depOverrides)

lazy val auth = project
  .in(file("auth"))
  .dependsOn(common)
  .settings(name := "chestnut-auth")
  .settings(version := (version in ThisBuild).value)
  .settings(scalacOptions ++= commonScalacOptions)
  .settings(Seq(jettyAlpnAgent, scalapbDeps))
  .enablePlugins(AkkaGrpcPlugin)
  .enablePlugins(JavaAgent)
  .enablePlugins(JavaAppPackaging)
  .enablePlugins(UniversalPlugin)
  .settings(depOverrides)

lazy val core = project
  .in(file("core"))
  .dependsOn(auth, common, idl)
  .settings(name := "chestnut-core")
  .settings(version := (version in ThisBuild).value)
  .settings(scalacOptions ++= commonScalacOptions)
  .settings(addKinkProjector)
  .enablePlugins(AkkaGrpcPlugin)
  .enablePlugins(JavaAgent)
  .enablePlugins(JavaAppPackaging)
  .enablePlugins(UniversalPlugin)
  .settings(depOverrides)
  .settings(chestnutCoreDeps)
  .settings(packagerSettings)



lazy val wartRemoverSettings = Seq(
  wartremoverWarnings ++= Warts.allBut(Wart.Equals)
)

val akkaV         = "2.6.4"
val akkaHttpV     = "10.1.11"
val akkaHttpJsonV = "1.29.1"
val slickV        = "3.3.2"
val catsV         = "2.1.0"
val circeV        = "0.13.0"
val prometheusV   = "0.8.0"
val nettyV        = "4.1.38.Final"
val silencerV     = "1.6.0"

lazy val jettyAlpnAgent   = javaAgents += "org.mortbay.jetty.alpn" % "jetty-alpn-agent" % "2.0.9" % "runtime;test"
lazy val scalapbDeps      = libraryDependencies += "com.thesamet.scalapb" %% "scalapb-runtime" % scalapb.compiler.Version.scalapbVersion % "protobuf"
lazy val addKinkProjector = addCompilerPlugin("org.typelevel" %% "kind-projector" % "0.10.3")

lazy val akka = libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http"   % akkaHttpV,
  "com.typesafe.akka" %% "akka-stream" % akkaV
)

lazy val commonScalacOptions = Seq(
  "-deprecation",
  "-encoding",
  "UTF-8",
  "-feature",
  "-unchecked",
  "-Ywarn-dead-code",
  "-language:postfixOps",
  "-language:higherKinds",
  "-language:existentials"
)

lazy val coverageSettings = Seq(
  coverageEnabled := false,
  coverageFailOnMinimum := true,
  coverageMinimum := 80,
  coverageOutputCobertura := false,
  coverageOutputXML := false
)

lazy val silencerSettings = libraryDependencies ++= Seq(
  compilerPlugin("com.github.ghik" % "silencer-plugin" % silencerV cross CrossVersion.full),
  "com.github.ghik" % "silencer-lib" % silencerV % Provided cross CrossVersion.full
)

addCommandAlias("validate", ";clean;protobuf:protobufGenerate;coverage;test;coverageReport")
addCommandAlias("package", ";core/universal:packageZipTarball")
addCommandAlias("stage", ";core/stage")
addCommandAlias("build", ";validate;coverageOff;package")
addCommandAlias("run", ";monitor/run")
addCommandAlias("pkg", ";core/universal:packageZipTarball")

ThisBuild / publishTo := Some("Artifactory Realm" at "https://repo1.iadmob.com/artifactory/libs-release")
ThisBuild / credentials += Credentials(Path.userHome / ".sbt" / ".credentials")

ThisBuild / sources in doc in Compile := List()
ThisBuild / publishArtifact in packageDoc := false
ThisBuild / publishArtifact in packageSrc := true

lazy val commonDeps = libraryDependencies ++= Seq(
  "com.typesafe.akka"           %%  "akka-actor-typed"      % akkaV,
  "org.typelevel"               %%  "cats-core"             % catsV,
  "io.circe"                    %%  "circe-core"            % circeV,
  "io.circe"                    %%  "circe-generic"         % circeV,
  "io.circe"                    %%  "circe-parser"          % circeV,
  "io.circe"                    %%  "circe-generic-extras"  % circeV,
  "io.prometheus"               %   "simpleclient"          % prometheusV,
  "io.prometheus"               %   "simpleclient_hotspot"  % prometheusV,
  "io.prometheus"               %   "simpleclient_common"   % prometheusV,
  "ch.qos.logback"              %   "logback-classic"       % "1.2.3",
  "com.typesafe.scala-logging"  %%  "scala-logging"         % "3.9.2",
  "com.github.pureconfig"       %%  "pureconfig"            % "0.12.1",
  "com.typesafe"                %   "config"                % "1.4.0",
  "com.norbitltd"               %%  "spoiwo"                % "1.6.1",
  "commons-io"                  %   "commons-io"            % "2.6",
  "net.dongliu"                 %   "apk-parser"            % "2.6.9",
  "com.github.daddykotex"       %%  "courier"               % "2.0.0",
  "com.pauldijou"               %% "jwt-circe"              % "3.0.1"
)

lazy val chestnutCoreDeps = libraryDependencies ++= Seq(
  "com.typesafe.slick"  %% "slick"               % slickV,
  "org.slf4j"           % "slf4j-nop"            % "1.7.26",
  "com.typesafe.slick"  %% "slick-hikaricp"      % slickV,
  "org.postgresql"      % "postgresql"           % "42.2.16",
  "com.github.tminglei" %% "slick-pg"            % "0.19.2",
  "com.github.tminglei" %% "slick-pg_circe-json" % "0.19.2"
)

lazy val depOverrides = libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-slf4j"           % akkaV,
  "com.typesafe.akka" %% "akka-actor"           % akkaV,
  "com.typesafe.akka" %% "akka-actor-typed"     % akkaV,
  "com.typesafe.akka" %% "akka-stream"          % akkaV,
  "com.typesafe.akka" %% "akka-cluster"         % akkaV,
  "com.typesafe.akka" %% "akka-cluster-tools"   % akkaV,
  "com.typesafe.akka" %% "akka-cluster-metrics" % akkaV,
  "com.typesafe.akka" %% "akka-discovery"       % akkaV,
  "com.typesafe.akka" %% "akka-http-core"       % akkaHttpV,
  "com.typesafe.akka" %% "akka-http"            % akkaHttpV,
  "com.typesafe.akka" %% "akka-http2-support"   % akkaHttpV,
  "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpV,
  "org.typelevel"     %% "cats-core"            % catsV,
  "org.typelevel"     %% "cats-free"            % catsV,
  "io.circe"          %% "circe-core"           % circeV,
  "io.circe"          %% "circe-generic"        % circeV,
  "io.circe"          %% "circe-parser"         % circeV,
  "io.circe"          %% "circe-generic-extras" % circeV
)

lazy val packagerSettings = Seq(mappings in Universal += baseDirectory.value / "deploy/app.sh" -> "app.sh") ++
  Option(System.getProperty("conf")).toList.map { conf =>
    mappings in Universal ++= {
      (baseDirectory.value / "deploy" / conf * "*").get.map { f =>
        f -> s"config/${f.name}"
      }
    }
  }
