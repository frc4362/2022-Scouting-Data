package com.gemsrobotics.scouting2022

import scalafx.geometry.{HPos, VPos}
import scalafx.scene.control.Label
import scalafx.scene.layout.GridPane

object Utils {
	implicit class bool2Int(bool: Boolean) {
		def toInt: Int =
			if (bool) 1 else 0
	}

	def makeHeaderText(myText: String): Label = new Label {
		text = myText
		style = "-fx-font-weight: bold; -fx-font-size: 20;"
		GridPane.setHalignment(this, HPos.Center)
		GridPane.setValignment(this, VPos.Bottom)
	}
}
