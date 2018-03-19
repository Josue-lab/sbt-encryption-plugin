
import sbt._

object Dependencies {

  private lazy val json4sVersion  = "3.2.10"
  private lazy val awsSdkVersion  = "1.11.103"

  lazy val ProjectDependencies = Seq(
    "com.amazonaws" % "aws-java-sdk-cloudformation" % awsSdkVersion,
    "com.amazonaws" % "aws-java-sdk-s3"             % awsSdkVersion,
    "com.amazonaws" % "aws-java-sdk-kms"            % awsSdkVersion,
    "com.amazonaws" % "aws-java-sdk-core"           % awsSdkVersion,
    "com.amazonaws" % "jmespath-java"               % awsSdkVersion,

    "org.json4s"    %% "json4s-native" % json4sVersion,
    "com.typesafe"  %   "config"       % "1.3.1",
    "org.typelevel" %% "cats-free"     % "0.9.0"
  )

  lazy val UnitTestDependencies = Seq(
    "org.scalamock" %% "scalamock-scalatest-support"  % "3.6.0"   % Test,
    "org.scalatest" %% "scalatest"                    % "3.0.1"   % Test
  )
}