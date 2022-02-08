package com.gemsrobotics.scouting2022

import scalafx.scene.control.{Label, Slider}
import scalafx.scene.layout.VBox

class LabelledSlider(
    title: String,
    sliderLength: Int = 3,
    textStyle: String = "-fx-font: 24px \"Sans\";"
) extends VBox {
	val slider = new Slider {
		snapToTicks = true
		showTickMarks = true
		showTickLabels = true
		blockIncrement = 1.0
		majorTickUnit = 1.0
		minorTickCount = 0
		max = sliderLength
		min = 0.0
	}

	private val label = new Label(title) {
		style = textStyle
	}

	spacing = 10
	children = Seq(label, slider)
}
