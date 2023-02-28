package com.gemsrobotics.scouting2022

import com.gemsrobotics.scouting2022.MultipleChoice.styleButton
import scalafx.scene.control.{RadioButton, ToggleGroup}

object MultipleChoice {
	def styleButton(button: RadioButton): Unit = {
		button.style = "-fx-font-weight: bold; -fx-font-size: 14;"
		button.scaleX = 1.35
		button.scaleY = 1.35
	}
}

class MultipleChoice(names: Vector[String]) {
	val buttons: Vector[RadioButton] = names.map(new RadioButton(_))
	val group = new ToggleGroup

	buttons.foreach { button =>
		styleButton(button)
		button.toggleGroup = group
		button.selected = false
	}

	def reset(): Unit =
		buttons.foreach { button =>
			button.selected = button.text.get == "None"
		}

	def isSelected: Boolean =
		buttons.exists(_.isSelected)

	def selected: Option[String] =
		buttons.find(_.isSelected).map(_.text.get)

	def selectedID: Option[Int] =
		selected.map(names.indexOf)
}
