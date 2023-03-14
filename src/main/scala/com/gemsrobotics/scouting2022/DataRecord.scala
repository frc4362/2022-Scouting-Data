package com.gemsrobotics.scouting2022

import com.gemsrobotics.scouting2022.Utils.bool2Int

import java.nio.file.{Files, Path, Paths}
import com.github.tototoshi.csv.{CSVFormat, CSVWriter}
import scalafx.beans.property.{IntegerProperty, StringProperty}
import scalafx.scene.control.{Alert, RadioButton}
import scalafx.scene.control.Alert.AlertType

import scala.util.Try
import scala.collection.JavaConverters._
import scala.collection.mutable

object DataRecord {
	private val SCHEMA_VERSION: String = "0.3"
	val EVENT_KEY: String = "2023mibel"

	private val HEADER: String =
		""""Schema Version","Event Key","Match #","Team #","Scout","Auton Mobility","Auton Charge","Auton Starting Pos","Endgame","Auton Cube Score High","Auton Cube Scored Mid","Auton Cube Scored Low","Auton Cone Scored High","Auton Cone Scored Mid","Auton Cone Scored Low","Teleop Cube Scored High","Teleop Cube Scored Mid","Teleop Cube Scored Low", "Teleop Cone Scored High","Teleop Cone Scored Mid","Teleop Cone Scored Low""""

	private val OUTPUT_PATH: Path =
		Paths.get(System.getProperty("user.dir") + "\\gemscoutput.csv")

	private implicit val format: CSVFormat =
		new MyCSVFormat

	private val writer: CSVWriter =
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
  val matchNumber: StringProperty,
  val teamNumber: StringProperty,
  val scoutName: StringProperty,

  val mobilityButton: RadioButton,
  val autonTaxiTypeQuestion: MultipleChoice,
  val autonStartingPositionQuestion: MultipleChoice,
  val teleopTaxiTypeQuestion: MultipleChoice,

  val autoTaxiProperty: IntegerProperty,
  val teleopTaxiProperty: IntegerProperty,

  val autonCubeScoredHigh: IntegerProperty,
  val autonCubeScoredMid: IntegerProperty,
  val autonCubeScoredLow: IntegerProperty,
  val autonConeScoredHigh: IntegerProperty,
  val autonConeScoredMid: IntegerProperty,
  val autonConeScoredLow: IntegerProperty,

  val teleopCubeScoredHigh: IntegerProperty,
  val teleopCubeScoredMid: IntegerProperty,
  val teleopCubeScoredLow: IntegerProperty,
  val teleopConeScoredHigh: IntegerProperty,
  val teleopConeScoredMid: IntegerProperty,
  val teleopConeScoredLow: IntegerProperty,

  val coolPoints: IntegerProperty
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

		// reset radio buttons
		mobilityButton.selected = false
		autonTaxiTypeQuestion.reset()
		autonStartingPositionQuestion.reset()
		teleopTaxiTypeQuestion.reset()

		autoTaxiProperty.value = 0
		teleopTaxiProperty.value = 0

		autonCubeScoredHigh.value = 0
		autonCubeScoredMid.value = 0
		autonCubeScoredLow.value = 0
		autonConeScoredHigh.value = 0
		autonConeScoredMid.value = 0
		autonConeScoredLow.value = 0

		teleopCubeScoredHigh.value = 0
		teleopCubeScoredMid.value = 0
		teleopCubeScoredLow.value = 0
		teleopConeScoredHigh.value = 0
		teleopConeScoredMid.value = 0
		teleopConeScoredLow.value = 0

		coolPoints.value = 0
	}

	def save(): Unit = {
		val problems: mutable.MutableList[String] = mutable.MutableList.empty[String]

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

		if (!autonStartingPositionQuestion.isSelected) {
			problems += "Auton starting position unspecified"
		}

		if (problems.isEmpty) {
			val fields = Seq(
				SCHEMA_VERSION,
				EVENT_KEY,
				matchNum,
				teamNum,
				name,

				mobilityButton.selected.value.toInt,
				autonTaxiTypeQuestion.selectedID.getOrElse(-1),
				autonStartingPositionQuestion.selectedID.getOrElse(-1),
				teleopTaxiProperty.get,

				autonCubeScoredHigh.get,
				autonCubeScoredMid.get,
				autonCubeScoredLow.get,
				autonConeScoredHigh.get,
				autonConeScoredMid.get,
				autonConeScoredLow.get,

				teleopCubeScoredHigh.get,
				teleopCubeScoredMid.get,
				teleopCubeScoredLow.get,
				teleopConeScoredHigh.get,
				teleopConeScoredMid.get,
				teleopConeScoredLow.get
			)

			writer.writeRow(fields)
			reset()
		} else {
			val content = "Missing required fields!\n" + problems.map(" - " ++ _ ++ "\n").mkString("")
			new Alert(AlertType.Error, content).showAndWait()
		}
	}
}
