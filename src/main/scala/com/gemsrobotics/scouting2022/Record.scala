package com.gemsrobotics.scouting2022

import java.nio.file.{Files, Path, Paths}

import com.github.tototoshi.csv.{CSVFormat, CSVWriter, QUOTE_ALL, Quoting}
import scalafx.beans.property.{BooleanProperty, IntegerProperty, StringProperty}
import scalafx.scene.control.Alert
import scalafx.scene.control.Alert.AlertType

import scala.util.Try
import scala.collection.JavaConverters._
import scala.collection.mutable.MutableList

object Record {
	private val SCHEMA_VERSION: String =
		"0.1"

	private val HEADER: String =
		""""Schema Version","Scout Name","Team Number","Match Number","Cargo Level 1","Cargo Level 2","Cargo Level 3","Cargo Cargo Ship","Level Climbed","Hatch Panel Level 1","Hatch Panel Level 2","Hatch Panel Level 3","Hatch Panel Cargo Ship","Sandstorm Bonus Level","Sandstorm Hatch Place","Sandstorm Cargo Place","Sandstorm Game Piece Pickup","Half Cycle","Drops under defense","Match Cargo Group","HP Group Score","Sand Storm Points","Hab Climb Group","Match Offense Score""""

	private val OUTPUT_PATH: Path =
		Paths.get(System.getProperty("user.dir") + "\\gemscoutput.csv")

	private class MyCSVFormat extends CSVFormat {
		val delimiter: Char = ','
		val quoteChar: Char = '"'
		val escapeChar: Char = '\\'
		val lineTerminator: String = "\r\n"
		val quoting: Quoting = QUOTE_ALL
		val treatEmptyLineAsNil: Boolean = false
	}

	private implicit val format: CSVFormat =
		new MyCSVFormat

	private val writer: CSVWriter =
		CSVWriter.open(OUTPUT_PATH.toFile, append=true)(format)

	def ensureOutputExists(): Unit = {
		val lines = Files.readAllLines(OUTPUT_PATH).asScala
		if (lines.headOption.getOrElse("") != HEADER) {
			println("Header not recognized!")
			val preprendedLines = (HEADER ++ "\n" ++ lines.mkString("\n")).getBytes
			Files.write(OUTPUT_PATH, preprendedLines)
		} else {
			println("File found and has header! Ready to store!")
		}
	}

	implicit class bool2Int(bool: Boolean) {
		def toInt: Int =
			if (bool) {
				1
			} else {
				0
			}
	}
}

class Record(
    matchNumber: StringProperty,
    teamNumber: StringProperty,
    scoutName: StringProperty,
    scoredCargo1: IntegerProperty,
    scoredCargo2: IntegerProperty,
    scoredCargo3: IntegerProperty,
    scoredPanels1: IntegerProperty,
    scoredPanels2: IntegerProperty,
    scoredPanels3: IntegerProperty,
    climbLevelAttempted: IntegerProperty,
    climbLevelSucceeded: BooleanProperty,
    sandstormStartingLevel: BooleanProperty,
    sandstormHatchPlace: IntegerProperty,
    sandstormPickup: IntegerProperty,
    defendedDrops: IntegerProperty,
    comments: StringProperty
) {
	import Record._

	def reset(): Unit = {
		matchNumber.value = Try(matchNumber.value.toInt)
				.toOption
				.map(_ + 1)
  			.map(_.toString)
  			.getOrElse("")
		teamNumber.value = ""
		scoredCargo1.value = 0
		scoredCargo2.value = 0
		scoredCargo3.value = 0
		scoredPanels1.value = 0
		scoredPanels2.value = 0
		scoredPanels3.value = 0
		climbLevelAttempted.value = 0
		climbLevelSucceeded.value = false
		sandstormStartingLevel.value = false
		sandstormHatchPlace.value = 0
		sandstormPickup.value = 0
		defendedDrops.value = 0
		comments.value = ""
	}

	def isValid: Boolean =
		(matchNumber.isNotEmpty and teamNumber.isNotEmpty and scoutName.isNotEmpty).get

	def save(): Unit = {
		val problems: MutableList[String] = MutableList.empty[String]

		val matchNum = matchNumber.get
		val teamNum = teamNumber.get
		val name = scoutName.get

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
				matchNum,
				teamNum,
				name,
				scoredCargo1.get,
				scoredCargo2.get,
				scoredCargo3.get,
				scoredPanels1.get,
				scoredPanels2.get,
				scoredPanels3.get,
				climbLevelAttempted.get,
				climbLevelSucceeded.get.toInt,
				sandstormStartingLevel.get.toInt,
				sandstormHatchPlace.get,
				sandstormPickup.get,
				defendedDrops.get,
				comments.get
			)

			writer.writeRow(fields)
			reset()
		} else {
			val content = "Missing required fields!\n" + problems.map(" - " ++ _ ++ "\n").mkString("")
			new Alert(AlertType.Error, content).showAndWait()
		}
	}
}
