package com.gemsrobotics.scouting2022

import com.github.tototoshi.csv.{CSVFormat, QUOTE_ALL, Quoting}

class MyCSVFormat extends CSVFormat {
	val delimiter: Char = ','
	val quoteChar: Char = '"'
	val escapeChar: Char = '\\'
	val lineTerminator: String = "\r\n"
	val quoting: Quoting = QUOTE_ALL
	val treatEmptyLineAsNil: Boolean = false
}
