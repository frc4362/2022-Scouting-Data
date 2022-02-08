package com.gemsrobotics.scouting2022

import scalafx.application.JFXApp
import scalafx.beans.property.{BooleanProperty, IntegerProperty}
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.Scene
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.control._
import scalafx.scene.layout.{GridPane, VBox}

object ScoutingApp extends JFXApp {
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

	val teamNumberLabel: Label = new Label("Team Number") {
		style = "-fx-font: 24px \"Sans\";"
	}
	val teamNumberTextField: TextField = new TextField

	val matchNumberLabel: Label = new Label("Match Number") {
		style = "-fx-font: 24px \"Sans\";"
	}
	val matchNumberTextField: TextField = new TextField

	val nameLabel: Label = new Label("Scout Name") {
		style = "-fx-font: 24px \"Sans\";"
	}
	val nameTextField: TextField = new TextField

	val cargoIncrementables: Seq[Incrementable] =
		Seq(new Incrementable("Cargo Level 1"),
			new Incrementable("Cargo Level 2"),
			new Incrementable("Cargo Level 3"))
	val hatchIncrementables: Seq[Incrementable] =
		Seq(new Incrementable("Hatch Level 1"),
			new Incrementable("Hatch Level 2"),
			new Incrementable("Hatch Level 3"))

	val climbLevelProperty: IntegerProperty =
		IntegerProperty(0)
	val climbLevelLabel: Label = new Label("Climb Level Attempted") {
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
		max = 3.0
		min = 0.0
	}

	val climbSucceededProperty: BooleanProperty =
		BooleanProperty(false)

	val climbSucceededLabel: Label =
		new Label("Climb Succeeded") {
			style = "-fx-font: 24px \"Sans\";"
		}
	val climbSucceededSwitch: ToggleSwitch =
		new ToggleSwitch
	val startingLevelLabel: Label =
		new Label("Sandstorm Starting Level") {
			style = "-fx-font: 24px \"Sans\";"
		}
	val startingLevelSwitch: ToggleSwitch =
		new ToggleSwitch("2", "1")
	val sandstormHatchesIncrementable: Incrementable =
		new Incrementable("Sandstorm Hatches Placed")
	val sandstormPickupsIncrementable: Incrementable =
		new Incrementable("Sandstorm Gamepiece Pickups")
	val piecesDroppedIncrementable: Incrementable =
		new Incrementable("Gamepieces Dropped (Defended)")
	val commentsLabel: Label = new Label("Comments") {
		style = "-fx-font: 24px \"Sans\";"
	}
	val commentsTextField: TextField = new TextField {
		prefHeight = 300
		alignment = Pos.TopLeft
	}

	val resetButton: Button =
		new Button("Reset") {
			prefWidth = 240
			prefHeight = 120
			style = "-fx-font-size: 24px;"
		}

	val saveButton: Button =
		new Button("Save") {
			padding = Insets(10, 10, 10, 10)
			prefWidth = 240
			prefHeight = 120
			style = s"-fx-background-color: ${Components.affirmativeGreen}; -fx-font-size: 24px;"
		}

	val record = new Record(
		matchNumberTextField.text,
		teamNumberTextField.text,
		nameTextField.text,
		cargoIncrementables.head.count,
		cargoIncrementables(1).count,
		cargoIncrementables(2).count,
		hatchIncrementables.head.count,
		hatchIncrementables(1).count,
		hatchIncrementables(2).count,
		climbLevelProperty,
		climbSucceededProperty,
		startingLevelSwitch.switchedOn,
		sandstormHatchesIncrementable.count,
		sandstormPickupsIncrementable.count,
		piecesDroppedIncrementable.count,
		commentsTextField.text
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

	val column4 = new VBox {
		spacing = 10
		children = Seq(
			commentsLabel,
			commentsTextField,
			resetButton, saveButton
		)
	}

	Record.ensureOutputExists()
	stage = new JFXApp.PrimaryStage {
		title.value = "Destination Deep Space Scouting App"
		scene = new Scene {
			stylesheets.add("stylesheet.css")
			content = new GridPane {
				alignment = Pos.Center
				hgap = 10
				padding = Insets(10, 10, 10, 10)
				// 1st column
				add(teamNumberLabel, 0, 0, 2, 1)
				add(teamNumberTextField, 0, 1, 2, 1)
				add(matchNumberLabel, 0, 2, 2, 1)
				add(matchNumberTextField, 0, 3, 2, 1)
				add(nameLabel, 0, 4, 2, 1)
				add(nameTextField, 0, 5, 2, 1)
				(6 to 8).foreach(n => add(cargoIncrementables(n - 6), 0, n))
				(6 to 8).foreach(n => add(hatchIncrementables(n - 6), 1, n))
				// 2nd column
				add(climbLevelLabel, 2, 0, 2, 1)
				add(climbLevelSlider, 2, 1, 2, 1)
				add(climbSucceededLabel, 2, 2, 2, 1)
				add(climbSucceededSwitch, 2, 3, 2, 1)
				add(startingLevelLabel, 2, 4, 2, 1)
				add(startingLevelSwitch, 2, 5, 2, 1)
				add(sandstormHatchesIncrementable, 2, 6, 2, 1)
				add(sandstormPickupsIncrementable, 2, 7, 2, 1)
				add(piecesDroppedIncrementable, 2, 8, 2, 1)
				// 3rd column
				add(column4, 4, 0, 2, 9)
			}
		}
	}

	stage.sizeToScene()
	stage.minWidth = stage.width.value
	stage.minHeight = stage.height.value
}
