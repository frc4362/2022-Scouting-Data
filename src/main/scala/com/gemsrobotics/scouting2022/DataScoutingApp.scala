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

		response match {
			case Some(ButtonType.Cancel) =>
				false
			case Some(ButtonType.OK) =>
				true
			case Some(_) =>
				false
			case None =>
				false
		}
	}

	val eventKeyLabel: Label = new Label("Event Key: " + DataRecord.EVENT_KEY)
	val teamNumberLabel: Label = new Label("Team #")
	val teamNumberTextField: TextField = new TextField
	val matchNumberLabel: Label = new Label("Match #")
	val matchNumberTextField: TextField = new TextField
	val nameLabel: Label = new Label("Name")
	val nameTextField: TextField = new TextField

	val taxiLevelProperty: IntegerProperty =
		IntegerProperty(0)
	val taxiLevelLabel: Label = new Label("Taxi?") {
		style = "-fx-font: 24px \"Sans\";"
	}

	val autonIncrementables: Seq[Incrementable] =
		Seq(new Incrementable("Low Scored"),
			new Incrementable("High Scored"),
			new Incrementable("Low Missed"),
			new Incrementable("High Missed")
		)

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
			prefWidth = 240
			prefHeight = 100
			style = style.value + s"-fx-background-color: ${Components.resetRed}; -fx-font-size: 24px;"
		}

	val saveButton: Button =
		new Button("Save") {
			prefWidth = 240
			prefHeight = 100
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
		climbLevelProperty
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

	val saveButtons = new VBox {
		spacing = 10
		children = Seq(
			resetButton,
			saveButton
		)
	}

	val topBox = new HBox {
		spacing = 10
		children = Seq(
			eventKeyLabel,
			teamNumberLabel,
			teamNumberTextField,
			matchNumberLabel,
			matchNumberTextField,
			nameLabel,
			nameTextField
		)
	}

	val taxiBox = new HBox {
		spacing = 10
		children = Seq(
			taxiLevelLabel,
			taxiLevelSlider,
			climbLevelLabel,
			climbLevelSlider
		)
	}

	val autonFields = new VBox {
		spacing = 5
		children = Seq(
			new Label("Auton Scoring"),
			autonIncrementables(0),
			autonIncrementables(1),
			autonIncrementables(2),
			autonIncrementables(3)
		)
	}

	val teleopFields = new VBox {
		spacing = 5
		children = Seq(
			new Label("Teleop Scoring"),
			teleopIncrementables(0),
			teleopIncrementables(1),
			teleopIncrementables(2),
			teleopIncrementables(3),
		)
	}

	val scoringFields = new HBox {
		spacing = 5
		children = Seq(
			autonFields,
			teleopFields
		)
	}

	DataRecord.ensureOutputExists()
	stage = new JFXApp.PrimaryStage {
		title.value = "Rapid React Scouting App"
		minHeight = 700
		minWidth = 1300
		maxHeight = 700
		maxWidth = 1300
		height = 700
		width = 1300
		resizable = false
		scene = new Scene {
			stylesheets.add("stylesheet.css")
			content = new GridPane {
				prefWidth = 1300
				prefHeight = 700
				alignment = Pos.Center
				hgap = 10
				padding = Insets(10, 10, 10, 10)
				background = new Background(Array(new BackgroundFill(Components.backgroundGrey, null, null)))
				add(topBox, 0, 0)
				add(taxiBox, 0, 1)
				add(scoringFields, 0, 3)
				add(saveButtons, 1, 3)
			}
		}
	}

	stage.sizeToScene()
	stage.minWidth = stage.width.value
	stage.minHeight = stage.height.value
}
