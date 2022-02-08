package com.gemsrobotics.scouting2022

import java.nio.file.{Files, Path, Paths}

import com.github.tototoshi.csv.{CSVFormat, CSVWriter}
import scalafx.beans.property.{IntegerProperty, StringProperty}
import scalafx.scene.control.Alert
import scalafx.scene.control.Alert.AlertType

import scala.util.Try
import scala.collection.JavaConverters._
import scala.collection.mutable.MutableList

object DataRecord {
	val SCHEMA_VERSION: String =
		"0.1"

	val EVENT_KEY: String =
		"2022gems"

	private val HEADER: String =
		""""Schema Version","Event Key","Team #","Match #","Scout","Taxi","Auton Scored Low","Auton Scored High","Auton Missed Low","Auton Missed High","Teleop Scored Low","Teleop Scored High","Teleop Missed Low","Teleop Missed High","Hang Level""""

	private val OUTPUT_PATH: Path =
		Paths.get(System.getProperty("user.dir") + "\\gemscoutput.csv")

	private implicit val format: CSVFormat =
		new MyCSVFormat

	val writer: CSVWriter =
		CSVWriter.open(OUTPUT_PATH.toFile, append=true)(format)

	def ensureOutputExists(): Unit = {
		val lines = Files.readAllLines(OUTPUT_PATH).asScala
		if (lines.headOption.getOrElse("") != HEADER) {
			println("Header not recognized!")
			val preprendedLines = (HEADER ++ "\r\n" ++ lines.mkString("\r\n")).getBytes
			Files.write(OUTPUT_PATH, preprendedLines)
		} else {
			println("File found and has header! Ready to store!")
		}
	}
}

class DataRecord(
    matchNumber: StringProperty,
    teamNumber: StringProperty,
    scoutName: StringProperty,
		autonTaxi: IntegerProperty,
		autonScoredLow: IntegerProperty,
		autonScoredHigh: IntegerProperty,
		autonMissedLow: IntegerProperty,
		autonMissedHigh: IntegerProperty,
		teleopScoredLow: IntegerProperty,
		teleopScoredHigh: IntegerProperty,
		teleopMissedLow: IntegerProperty,
		teleopMissedHigh: IntegerProperty,
    climbLevel: IntegerProperty,
) {
	import DataRecord._

	def reset(): Unit = {
		matchNumber.value = Try(matchNumber.value.toInt)
				.toOption
				.map(_ + 1)
  			.map(_.toString)
  			.getOrElse("")
		teamNumber.value = ""
		// scoutName doesn't reset
		autonTaxi.value = 0
		autonScoredLow.value = 0
		autonScoredHigh.value = 0
		autonMissedLow.value = 0
		autonMissedHigh.value = 0
		teleopScoredLow.value = 0
		teleopScoredHigh.value = 0
		teleopMissedLow.value = 0
		teleopMissedHigh.value = 0
		climbLevel.value = 0
	}

	def isValid: Boolean =
		(matchNumber.isNotEmpty and teamNumber.isNotEmpty and scoutName.isNotEmpty).get

	def save(): Unit = {
		val problems: MutableList[String] = MutableList.empty[String]

		val matchNum = matchNumber.get
		val teamNum = teamNumber.get
		val name = scoutName.get

		if (EVENT_KEY.isEmpty) {
			problems += "Event key may not be empty (Talk to a programmer, your app needs to be fixed)"
		}

		if (matchNum.isEmpty) {
			problems += "Match number not specified"
		}

		if (teamNum.isEmpty) {
			problems += "Team number not specified"
		}

		if (name.isEmpty) {
			problems += "Scout name unspecified"
		}

		if (problems.isEmpty) {
			val fields = Seq(
				SCHEMA_VERSION,
				EVENT_KEY,
				teamNum,
				matchNum,
				name,
				autonTaxi.get,
				autonScoredLow.get,
				autonScoredHigh.get,
				autonMissedLow.get,
				autonMissedHigh.get,
				teleopScoredLow.get,
				teleopScoredHigh.get,
				teleopMissedLow.get,
				teleopMissedHigh.get,
				climbLevel.get
			)

			writer.writeRow(fields)
			reset()
		} else {
			val content = "Missing required fields!\n" + problems.map(" - " ++ _ ++ "\n").mkString("")
			new Alert(AlertType.Error, content).showAndWait()
		}
	}
}
