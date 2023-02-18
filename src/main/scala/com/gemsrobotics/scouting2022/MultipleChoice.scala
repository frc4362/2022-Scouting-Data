package com.gemsrobotics.scouting2022

import scalafx.scene.control.{RadioButton, ToggleGroup}

class MultipleChoice(names: Vector[String]) {
	val buttons: Vector[RadioButton] = names.map(new RadioButton(_))
	val group = new ToggleGroup

	buttons.foreach { button =>
		button.style = "-fx-font-weight: bold; -fx-font-size: 14;"
		button.toggleGroup = group
		button.selected = false
		button.scaleX = 1.35
		button.scaleY = 1.35
	}

	def reset(): Unit =
		buttons.foreach(_.selected = false)

	def isSelected: Boolean =
		buttons.exists(_.isSelected)

	def selected: Option[String] =
		buttons.find(_.isSelected).map(_.text.get)

	def selectedID: Option[Int] =
		selected.map(names.indexOf)
}
