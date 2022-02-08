package com.gemsrobotics.scouting2022

object Utils {
	implicit class bool2Int(bool: Boolean) {
		def toInt: Int =
			if (bool) {
				1
			} else {
				0
			}
	}
}
