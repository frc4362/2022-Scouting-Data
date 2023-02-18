package com.gemsrobotics.scouting2022

import com.gemsrobotics.scouting2022.DataScoutingApp.teamNumberLabel
import com.gemsrobotics.scouting2022.Utils.makeHeaderText
import scalafx.application.JFXApp
import scalafx.beans.binding.{Bindings, StringBinding}
import scalafx.beans.property.{IntegerProperty, StringProperty}
import scalafx.geometry.{HPos, Insets, Pos, VPos}
import scalafx.scene.Scene
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.control._
import scalafx.scene.layout.{Background, BackgroundFill, GridPane, HBox, Priority, VBox}

import scala.::
import scala.collection.immutable.Nil.:::

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

	def formatButton(button: RadioButton, group: ToggleGroup): Unit = {
		button.style = "-fx-font-weight: bold; -fx-font-size: 14;"
		button.setToggleGroup(group)
		button.setSelected(false)
		button.scaleX = 1.35
		button.scaleY = 1.35
	}

	// Auton taxi buttons
	val autonTaxiTypeGroup: ToggleGroup = new ToggleGroup
	val autonTaxiTypeButtons: Seq[RadioButton] = Seq(
		new RadioButton("Mobility"),
		new RadioButton("Docked"),
		new RadioButton("Engaged")
	)

	autonTaxiTypeButtons.foreach(formatButton(_, autonTaxiTypeGroup))

	// Auton starting buttons
	val autonStartingPositionGroup: ToggleGroup = new ToggleGroup
	val autonStartingPositionButtons: Seq[RadioButton] = Seq(
		new RadioButton("Left"),
		new RadioButton("Center"),
		new RadioButton("Right")
	)

	autonStartingPositionButtons.foreach(formatButton(_, autonStartingPositionGroup))

	val autonStartingPositionProperty: StringProperty = new StringProperty
	val autonStartingPositionBinding: StringBinding = Bindings.createStringBinding(() => {
		// TODO
		"To Do"
	}, autonStartingPositionProperty)

	val autonTaxiTypeProperty: StringProperty = new StringProperty
	val autonTaxiTypeBinding: StringBinding = Bindings.createStringBinding(() => {
		// TODO
		"To Do"
	}, autonTaxiTypeProperty)

	// Teleop taxi buttons
	val teleopTaxiTypeGroup: ToggleGroup = new ToggleGroup
	val teleopTaxiTypeButtons: Seq[RadioButton] = Seq(
		new RadioButton("Parked"),
		new RadioButton("Docked"),
		new RadioButton("Engaged")
	)

	teleopTaxiTypeButtons.foreach(formatButton(_, teleopTaxiTypeGroup))

	val teleopTaxiTypeProperty: StringProperty = new StringProperty
	val teleopTaxiTypeBinding = Bindings.createStringBinding(() => {
		// TODO
		"To Do"
	}, teleopTaxiTypeProperty)

	def newIncrementableGroup: Seq[Incrementable] =
		Seq(new Incrementable("High Scored"),
			new Incrementable("Mid Scored"),
			new Incrementable("Low Scored"))

	val autonConeIncrementables: Seq[Incrementable] = newIncrementableGroup
	autonConeIncrementables.foreach(_.setButtonColor(MyColors.ConeYellow))
	val autonCubeIncrementables: Seq[Incrementable] = newIncrementableGroup
	autonCubeIncrementables.foreach(_.setButtonColor(MyColors.CubePurple))
	val teleopConeIncrementables: Seq[Incrementable] = newIncrementableGroup
	teleopConeIncrementables.foreach(_.setButtonColor(MyColors.ConeYellow))
	val teleopCubeIncrementables: Seq[Incrementable] = newIncrementableGroup
	teleopCubeIncrementables.foreach(_.setButtonColor(MyColors.CubePurple))

	val coolIncrementable = new Incrementable("Cool Points")
	coolIncrementable.setButtonColor(MyColors.CoolPink)

	val commentsLabel: Label = new Label("Comments") {
		style = "-fx-font: 24px \"Sans\";"
	}

	val resetButton: Button =
		new Button("Reset") {
			prefWidth = 280
			prefHeight = 100
			style = style.value + s"-fx-background-color: ${MyColors.ResetRed}; -fx-font-size: 24px; -fx-text-fill: black;"
		}

	val saveButton: Button =
		new Button("Save") {
			prefWidth = 280
			prefHeight = 100
			style = style.value + s"-fx-background-color: ${MyColors.AffirmativeGreen}; -fx-font-size: 24px; -fx-text-fill: black;"
		}

	val teleopScoringBox: ScoringBox = new ScoringBox
	val autonScoringBox: ScoringBox = new ScoringBox

	val record = new DataRecord(
		matchNumberTextField.text,
		teamNumberTextField.text,
		nameTextField.text,

		autonTaxiTypeButtons,
		autonStartingPositionButtons,
		teleopTaxiTypeButtons,

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

	val climbBox = new HBox {
		spacing = 10
		children = Seq()
	}

	val autonTextBox = new Label("Auton Scoring")
	autonTextBox.style = "-fx-font-weight: bold"

	val teleopTextBox = new Label("Teleop Scoring")
	teleopTextBox.style = "-fx-font-weight: bold"

	val autonStartingPositionQuestion = new HBox {
		spacing = 90
		children = Seq(new Label("")) ++ autonStartingPositionButtons
		GridPane.setHalignment(this, HPos.Center)
	}

	def makeTaxiQuestion(buttons: Seq[RadioButton]): VBox = new VBox {
		spacing = 30
		children = buttons
		alignment = Pos.Center
	}

	val saveRow = new HBox {
		spacing = 10
		children = Seq(
			saveButton,
			resetButton
		)
	}

	val WINDOW_HEIGHT: Int = 900
	val WINDOW_WIDTH: Int = 1300

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
				background = new Background(Array(new BackgroundFill(MyColors.BackgroundGrey, null, null)))
				add(header, 0, 0)
				add(saveRow, 1, 0)
				add(makeHeaderText("Auton"), 0, 1)
				add(makeHeaderText("Teleop"), 1, 1)
				add(autonStartingPositionQuestion, 0, 2)
				add(autonScoringBox, 0, 3)
				add(teleopScoringBox, 1, 3)
				add(makeTaxiQuestion(autonTaxiTypeButtons), 0, 4)
				add(makeTaxiQuestion(teleopTaxiTypeButtons), 1, 4)
			}
		}
	}

	DataRecord.ensureOutputExists()
	stage.sizeToScene()
	stage.minWidth = stage.width.value
	stage.minHeight = stage.height.value
}
