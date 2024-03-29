package com.gemsrobotics.scouting2022

import scalafx.beans.property.IntegerProperty
import scalafx.geometry.Pos
import scalafx.scene.control.{Button, Label}
import scalafx.scene.layout.{HBox, VBox}
import scalafx.scene.text.{Font, FontWeight, TextAlignment}

protected class MyButton(text: String) extends Button(text) {
	prefWidth = 100
	prefHeight = 10
	font = Font("Consolas", FontWeight.Bold, 20)
}

class Incrementable(val name: String, val startingValue: Int = 0) extends VBox {
	private val countLabel = new Label {
		text = startingValue.toString
		font = Font("Tahoma", FontWeight.Normal, 36)
		alignment = Pos.BaselineCenter
		textAlignment = TextAlignment.Center
		prefWidth = 215
	}

	private val nameLabel = new Label {
		text = name
		font = Font("Tahoma", FontWeight.Normal, 24)
		alignment = Pos.BaselineCenter
		textAlignment = TextAlignment.Center
	}

	val count = IntegerProperty(0)
	count.onChange { (_, _, newValue) =>
		countLabel.text = newValue.toString
	}

	val buttons = Seq(
		new MyButton("-") {
			onMouseClicked = { _ =>
				if (count.value > 0) {
					count.value -= 1
				}
			}
		},
		new MyButton("+") {
			onMouseClicked = { _ =>
				count.value += 1
			}
		}
	)

	private val myButtons = new HBox {
		spacing = 10
		children = buttons
	}

	def setButtonColor(color: String): Unit =
		buttons.foreach { button =>
			button.style.value = button.style.value + s"-fx-background-color: ${color};"
		}

	children = Seq(
		nameLabel,
		countLabel,
		myButtons
	)
}
