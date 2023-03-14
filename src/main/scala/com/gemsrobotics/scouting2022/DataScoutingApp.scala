package com.gemsrobotics.scouting2022

import com.gemsrobotics.scouting2022.Utils.makeHeaderText
import scalafx.application.JFXApp
import scalafx.beans.binding.Bindings
import scalafx.beans.property.IntegerProperty
import scalafx.geometry.{HPos, Insets, Pos, VPos}
import scalafx.scene.Scene
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.control._
import scalafx.scene.layout.{Background, BackgroundFill, GridPane, HBox, VBox}
import scalafx.scene.text.TextAlignment

object DataScoutingApp extends JFXApp {
	def userConfirm(question: String): Boolean = {
		val response: Option[ButtonType] =
			new Alert(AlertType.Confirmation, question, ButtonType.Cancel, ButtonType.OK) {
				title = "?"
			}.showAndWait()

		response.contains(ButtonType.OK)
	}

	val eventKeyLabel: Label = new Label("Event Key: " + DataRecord.EVENT_KEY)
	eventKeyLabel.style = "-fx-font-weight: bold"
	val teamNumberLabel: Label = new Label("Team #")
	val teamNumberTextField: TextField = new TextField
	val matchNumberLabel: Label = new Label("Match #")
	val matchNumberTextField: TextField = new TextField
	val nameLabel: Label = new Label("    Name")
	val nameTextField: TextField = new TextField

	val autonMobilityButton = new RadioButton("Mobility")
	MultipleChoice.styleButton(autonMobilityButton)

	// Auton taxi buttons
	val autonTaxiQuestion = new MultipleChoice(Vector("None", "Docked", "Engaged"))

	// Auton starting buttons
	val autonStartingPositionQuestion = new MultipleChoice(Vector("Left", "Center", "Right"))

	// Teleop taxi buttons
	val teleopTaxiQuestion = new MultipleChoice(Vector("None", "Parked", "Docked", "Engaged"))

	val coolIncrementable = new Incrementable("Cool Points")
	coolIncrementable.setButtonColor(MyColors.CoolPink)

	val resetButton: Button =
		new Button("Reset") {
			prefWidth = 200
			prefHeight = 80
			style = style.value + s"-fx-background-color: ${MyColors.ResetRed}; -fx-font-size: 24px; -fx-text-fill: black;"
		}

	val saveButton: Button =
		new Button("Save") {
			prefWidth = 200
			prefHeight = 80
			style = style.value + s"-fx-background-color: ${MyColors.AffirmativeGreen}; -fx-font-size: 24px; -fx-text-fill: black;"
		}

	val teleopScoringBox: ScoringBox = new ScoringBox
	val autonScoringBox: ScoringBox = new ScoringBox


	val teleopTaxiLevels = Vector("None", "Parked", "Docked", "Engaged")

	val teleopTaxiProperty = new IntegerProperty

	val teleopTaxiSlider = new Slider {
		value <==> teleopTaxiProperty
		snapToTicks = true
		snapToTicks = true
		showTickMarks = true
		showTickLabels = false // maybe should be true?
		blockIncrement = 1.0
		majorTickUnit = 1.0
		minorTickCount = 0
		max = 3.0
		min = 0.0
	}

	val teleopTaxiLabel = new Label("None") {
		alignment = Pos.BaselineCenter
		textAlignment = TextAlignment.Center
		GridPane.setHalignment(this, HPos.Center)
	}

	teleopTaxiProperty.addListener { (o, oldValue, newValue) =>
		teleopTaxiLabel.text = teleopTaxiLevels(newValue.intValue).toString
	}

	val teleopTaxiBox = new VBox {
		spacing = 5
		padding = Insets(25, 0, 0, 0)
		alignment = Pos.BaselineCenter
		children = Seq(teleopTaxiSlider, teleopTaxiLabel)
	}

	val autoTaxiLevels = Vector("None", "Docked", "Engaged")

	val autoTaxiProperty = new IntegerProperty

	val autoTaxiSlider = new Slider {
		value <==> teleopTaxiProperty
		snapToTicks = true
		snapToTicks = true
		showTickMarks = true
		showTickLabels = false // maybe should be true?
		blockIncrement = 1.0
		majorTickUnit = 1.0
		minorTickCount = 0
		max = 2.0
		min = 0.0
	}

	val autoTaxiLabel = new Label("None") {
		alignment = Pos.BaselineCenter
		textAlignment = TextAlignment.Center
		GridPane.setHalignment(this, HPos.Center)
	}

	autoTaxiProperty.addListener { (o, oldValue, newValue) =>
		autoTaxiLabel.text = autoTaxiLevels(newValue.intValue).toString
	}

	val autoTaxiBox = new VBox {
		spacing = 5
		padding = Insets(25, 0, 0, 0)
		alignment = Pos.BaselineCenter
		children = Seq(autonMobilityButton, autoTaxiSlider, autoTaxiLabel)
	}

	val record = new DataRecord(
		matchNumberTextField.text,
		teamNumberTextField.text,
		nameTextField.text,

		autonMobilityButton,
		autonTaxiQuestion,
		autonStartingPositionQuestion,
		teleopTaxiQuestion,

		autoTaxiProperty,
		teleopTaxiProperty,

		autonScoringBox.incrementables(0).count,
		autonScoringBox.incrementables(1).count,
		autonScoringBox.incrementables(2).count,
		autonScoringBox.incrementables(3).count,
		autonScoringBox.incrementables(4).count,
		autonScoringBox.incrementables(5).count,

		teleopScoringBox.incrementables(0).count,
		teleopScoringBox.incrementables(1).count,
		teleopScoringBox.incrementables(2).count,
		teleopScoringBox.incrementables(3).count,
		teleopScoringBox.incrementables(4).count,
		teleopScoringBox.incrementables(5).count,

		coolIncrementable.count
	)

	resetButton.onMouseClicked = { _ =>
		if (userConfirm("Are you sure you want to reset?\nTHIS IS IRREVERSIBLE")) {
			record.reset()
		}
	}

	saveButton.onMouseClicked = { _ =>
		if (userConfirm("Are you sure you want to save?")) {
			record.save()
		}
	}

	// Event fields
	val eventBox = new HBox {
		spacing = 20
		children = Seq(
			eventKeyLabel,
			nameLabel,
			nameTextField)
	}

	val matchBox = new HBox {
		spacing = 20
		children = Seq(
			teamNumberLabel,
			teamNumberTextField,
			matchNumberLabel,
			matchNumberTextField
		)
	}

	val header = new VBox {
		spacing = 5
		children = Seq(
			eventBox,
			matchBox
		)
	}

	val autonTextBox = new Label("Auton Scoring")
	autonTextBox.style = "-fx-font-weight: bold"

	val teleopTextBox = new Label("Teleop Scoring")
	teleopTextBox.style = "-fx-font-weight: bold"

	val autonStartingPositionQuestionBox = new HBox {
		spacing = 90
		children = Seq(new Label("")) ++ autonStartingPositionQuestion.buttons
		GridPane.setHalignment(this, HPos.Center)
	}

	def makeTaxiQuestionBox(question: Seq[RadioButton]): VBox = new VBox {
		spacing = 15
		children = question
		alignment = Pos.Center
	}

	val saveRow = new HBox {
		spacing = 10
		children = Seq(
			saveButton,
			resetButton
		)
	}

	val WINDOW_HEIGHT: Int = 680
	val WINDOW_WIDTH: Int = 1100

	stage = new JFXApp.PrimaryStage {
		title.value = "Charged Up Scouting App"
		minHeight = WINDOW_HEIGHT
		minWidth = WINDOW_WIDTH
		maxHeight = WINDOW_HEIGHT
		maxWidth = WINDOW_WIDTH
		fullScreen = false
		resizable = false
		scene = new Scene(WINDOW_WIDTH, WINDOW_HEIGHT) {
			stylesheets.add("stylesheet.css")
			root = new GridPane {
				alignment = Pos.TopLeft
				hgap = 0
				vgap = 5
				padding = Insets(0, 10, 0, 10)
				background = new Background(Array(new BackgroundFill(MyColors.BackgroundGrey, null, null)))
				add(header, 0, 0)
				add(saveRow, 1, 0)
				add(makeHeaderText("Auton"), 0, 1)
				add(makeHeaderText("Teleop"), 1, 1)
				add(autonStartingPositionQuestionBox, 0, 2)
				add(autonScoringBox, 0, 3)
				add(teleopScoringBox, 1, 3)
				add(makeTaxiQuestionBox(autonMobilityButton :: autonTaxiQuestion.buttons.toList), 0, 4)
//				add(makeTaxiQuestionBox(teleopTaxiQuestion.buttons.toList), 1, 4)
				add(teleopTaxiBox, 1, 4)
			}
		}
	}

	DataRecord.ensureOutputExists()
	stage.sizeToScene()
	stage.minWidth = stage.width.value
	stage.minHeight = stage.height.value
}
