package com.gemsrobotics.scouting2022

import scalafx.beans.property.BooleanProperty
import scalafx.geometry.Pos
import scalafx.scene.control.{Button, Label}
import scalafx.scene.layout.HBox

class ToggleSwitch(
    val messageAffirmative: String = "Yes",
    val messageNegative: String = "No"
) extends HBox {
	private val STYLE_SWITCH: String =
		"-fx-background-color: grey; -fx-text-fill:black; -fx-background-radius: 4;"
	private val STYLE_AFFIRMATIVE: String =
		s"-fx-background-color: ${MyColors.AffirmativeGreen};"
	private val STYLE_NEGATIVE: String =
		"-fx-background-color: grey;"

	private val label = new Label()
	private val button = new Button()

	val switchedOn: BooleanProperty = BooleanProperty(false)

	init()
	switchedOn.onChange { (_, _, newValue) =>
		if (newValue) {
			label.text = messageAffirmative
			style = STYLE_AFFIRMATIVE
			label.toFront()
		} else {
			label.text = messageNegative
			style = STYLE_NEGATIVE
			button.toFront()
		}
	}

	def toggle(): Unit = {
			switchedOn.value = !switchedOn.get
	}

	private def init(): Unit = {
		label.text = messageNegative
		children.addAll(label, button)
		button.onMouseClicked = { _ =>
			toggle()
		}
		label.onMouseClicked = { _ =>
			toggle()
		}

		label.alignment = Pos.Center
		style = STYLE_SWITCH
		alignment = Pos.CenterLeft

		label.prefWidth.bind(width / 2)
		label.prefHeight.bind(height)
		button.prefWidth.bind(width / 2)
		button.prefHeight.bind(height)
	}
}
