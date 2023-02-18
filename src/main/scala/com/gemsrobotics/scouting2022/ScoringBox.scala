package com.gemsrobotics.scouting2022

import com.gemsrobotics.scouting2022.Utils.makeHeaderText
import scalafx.geometry.{HPos, Insets, Pos, VPos}
import scalafx.scene.control.Label
import scalafx.scene.layout.{GridPane, HBox, VBox}
import scalafx.scene.text.{Font, FontWeight, TextAlignment}

class ScoringBox extends VBox {
	private val highCone = new Incrementable("")
	highCone.setButtonColor(MyColors.ConeYellow)
	private val highCube = new Incrementable("")
	highCube.setButtonColor(MyColors.CubePurple)
	private val midCone = new Incrementable("")
	midCone.setButtonColor(MyColors.ConeYellow)
	private val midCube = new Incrementable("")
	midCube.setButtonColor(MyColors.CubePurple)
	private val lowCone = new Incrementable("")
	lowCone.setButtonColor(MyColors.ConeYellow)
	private val lowCube = new Incrementable("")
	lowCube.setButtonColor(MyColors.CubePurple)

	spacing = 6
	children = new GridPane {
		alignment = Pos.Center
		hgap = 5
		vgap = 5
		padding = Insets(0, 0, 0, 0)
		add(makeHeaderText("Cone"), 1, 0)
		add(makeHeaderText("Cube"), 2, 0)
		add(makeHeaderText("High"), 0, 1)
		add(highCone, 1, 1)
		add(highCube, 2, 1)
		add(makeHeaderText("Mid"), 0, 2)
		add(midCone, 1, 2)
		add(midCube, 2, 2)
		add(makeHeaderText("Low"), 0, 3)
		add(lowCone, 1, 3)
		add(lowCube, 2, 3)
	}

	def incrementables: Seq[Incrementable] = Seq(
		highCube,
		midCube,
		lowCube,
		highCone,
		midCone,
		lowCone
	)
}
