package com.gemsrobotics.scouting2022

import scalafx.scene.control.TextField
import scalafx.scene.paint.Color

object Components {
	val sandstormYellow: String =
		"#f0e150"

	val affirmativeGreen: String =
		"#37d964"

	val resetRed: String =
		"#DC143C"

	val rocketBlue: String =
		"#6d9bdb"

	val backgroundGrey: Color =
		Color.web("#303030")

	def textField(field: String): TextField =
		new TextField {
			text = field
		}
}
