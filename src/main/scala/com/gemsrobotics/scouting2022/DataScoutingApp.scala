package com.gemsrobotics.scouting2022

import scalafx.application.JFXApp
import scalafx.beans.property.IntegerProperty
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.Scene
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.control._
import scalafx.scene.layout.{Background, BackgroundFill, GridPane, HBox, VBox}

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
	val emptyLabel: Label = new Label("")

	val taxiLevelProperty: IntegerProperty =
		IntegerProperty(0)
	val taxiLevelLabel: Label = new Label("Taxi?") {
		style = "-fx-font: 24px \"Sans\";"
	}

	val autonIncrementables: Seq[Incrementable] =
		Seq(new Incrementable("Low Scored"),
			new Incrementable("High Scored"),
			new Incrementable("Low Missed"),
			new Incrementable("High Missed"))

	val teleopIncrementables: Seq[Incrementable] =
		Seq(new Incrementable("Low Scored"),
			new Incrementable("High Scored"),
			new Incrementable("Low Missed"),
			new Incrementable("High Missed"))

	autonIncrementables.flatMap(_.btns).foreach { btn =>
		btn.style.value = btn.style.value + s"-fx-background-color: ${Components.resetRed};"
	}
	teleopIncrementables.flatMap(_.btns).foreach { btn =>
		btn.style.value = btn.style.value + s"-fx-background-color: ${Components.rocketBlue};"
	}

	val coolIncrementable = new Incrementable("Cool Points")
	coolIncrementable.btns.foreach { btn =>
		btn.style.value = btn.style.value + s"-fx-background-color: ${Components.coolPink};"
	}

	val taxiLevelSlider: Slider = new Slider {
		value <==> taxiLevelProperty
		snapToTicks = true
		showTickMarks = true
		showTickLabels = true
		blockIncrement = 1.0
		majorTickUnit = 1.0
		minorTickCount = 0
		max = 1.0
		min = 0.0
	}

	val climbLevelProperty: IntegerProperty =
		IntegerProperty(0)
	val climbLevelLabel: Label = new Label("Level Climbed") {
		style = "-fx-font: 24px \"Sans\";"
	}

	val climbLevelSlider: Slider = new Slider {
		value <==> climbLevelProperty
		snapToTicks = true
		showTickMarks = true
		showTickLabels = true
		blockIncrement = 1.0
		majorTickUnit = 1.0
		minorTickCount = 0
		max = 4.0
		min = 0.0
	}

	val commentsLabel: Label = new Label("Comments") {
		style = "-fx-font: 24px \"Sans\";"
	}

	val resetButton: Button =
		new Button("Reset") {
			prefWidth = 300
			prefHeight = 120
			style = style.value + s"-fx-background-color: ${Components.resetRed}; -fx-font-size: 24px;"
		}

	val saveButton: Button =
		new Button("Save") {
			prefWidth = 300
			prefHeight = 120
			style = style.value + s"-fx-background-color: ${Components.affirmativeGreen}; -fx-font-size: 24px;"
		}

	val record = new DataRecord(
		matchNumberTextField.text,
		teamNumberTextField.text,
		nameTextField.text,
		taxiLevelProperty,
		autonIncrementables(0).count,
		autonIncrementables(1).count,
		autonIncrementables(2).count,
		autonIncrementables(3).count,
		teleopIncrementables(0).count,
		teleopIncrementables(1).count,
		teleopIncrementables(2).count,
		teleopIncrementables(3).count,
		climbLevelProperty,
		coolIncrementable.count)

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

	val topBox = new HBox {
		spacing = 10
		children = Seq(
			eventKeyLabel,
			nameLabel,
			nameTextField)
	}

	val taxiBox = new HBox {
		spacing = 10
		children = Seq(
			teamNumberLabel,
			teamNumberTextField,
			matchNumberLabel,
			matchNumberTextField,
			taxiLevelLabel,
			taxiLevelSlider)
	}

	val climbBox = new HBox {
		spacing = 10
		children = Seq(
			climbLevelLabel,
			climbLevelSlider)
	}


	val autonTextBox = new Label("Auton Scoring")
	autonTextBox.style = "-fx-font-weight: bold"

	val autonFields = new VBox {
		spacing = 5
		children = Seq(
			autonTextBox,
			autonIncrementables(1),
			autonIncrementables(0),
			autonIncrementables(3),
			autonIncrementables(2))
	}

	val teleopTextBox = new Label("Teleop Scoring")
	teleopTextBox.style = "-fx-font-weight: bold"

	val teleopFields = new VBox {
		spacing = 5
		children = Seq(
			teleopTextBox,
			teleopIncrementables(1),
			teleopIncrementables(0),
			teleopIncrementables(3),
			teleopIncrementables(2))
	}

	val saveButtons = new VBox {
		spacing = 10
		children = Seq(
			emptyLabel,
			coolIncrementable,
			climbBox,
			resetButton,
			saveButton)
	}

	val scoringFields = new HBox {
		spacing = 10
		children = Seq(
			autonFields,
			teleopFields,
			saveButtons)
	}

	val WINDOW_HEIGHT: Int = 660
	val WINDOW_WIDTH: Int = 800

	stage = new JFXApp.PrimaryStage {
		title.value = "Rapid React Scouting App"
		minHeight = WINDOW_HEIGHT
		minWidth = WINDOW_WIDTH
		maxHeight = WINDOW_HEIGHT
		maxWidth = WINDOW_WIDTH
		height = WINDOW_HEIGHT
		width = WINDOW_WIDTH
		fullScreen = false
		resizable = false
		scene = new Scene(WINDOW_WIDTH, WINDOW_HEIGHT) {
			stylesheets.add("stylesheet.css")
			root = new GridPane {
				alignment = Pos.TopLeft
				hgap = 20
				vgap = 10
				padding = Insets(10, 40, 10, 40)
				background = new Background(Array(new BackgroundFill(Components.backgroundGrey, null, null)))
				add(topBox, 0, 0)
				add(taxiBox, 0, 1)
				add(scoringFields, 0, 2)
			}
		}
	}

	DataRecord.ensureOutputExists()
	stage.sizeToScene()
	stage.minWidth = stage.width.value
	stage.minHeight = stage.height.value
}
