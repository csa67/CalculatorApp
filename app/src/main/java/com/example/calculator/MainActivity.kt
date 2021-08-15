package com.example.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import org.w3c.dom.Text

class MainActivity : AppCompatActivity() {

    var hasDecimal = false
    var canAddOp = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun digitPressed(view: View) {

        val edtCalc = findViewById<TextView>(R.id.edtCalc)

        if (view is Button) {
            if (view.id == R.id.btnDot) {
                if (!hasDecimal) {
                    edtCalc.append(view.text)
                }
                hasDecimal = true
            } else {
                edtCalc.append(view.text)
            }
            canAddOp = true
        }

    }

    fun opPressed(view: View) {

        val edtCalc = findViewById<TextView>(R.id.edtCalc)

        if (view is Button && canAddOp) {
            edtCalc.append(view.text)
            canAddOp = false
            hasDecimal = false
        }

    }

        fun percentPressed(view: View) {

            val edtCalc = findViewById<TextView>(R.id.edtCalc)
            var calculation = edtCalc.text.toString()
            val result = findViewById<TextView>(R.id.txtResult)

            if (view is Button) {

                val number = calculation.toDouble() / 100
                edtCalc.append(view.text)
                result.text = number.toString()
            }

        }

        fun clearPressed(view: View) {

            val edtCalc = findViewById<TextView>(R.id.edtCalc)
            val result = findViewById<TextView>(R.id.txtResult)

            edtCalc.text = ""
            result.text = ""
            hasDecimal = false

        }

        fun equalPressed(view: View) {

            var result = findViewById<TextView>(R.id.txtResult)
            result.text = calculateResult()
        }

        private fun calculateResult(): String {

            val digitsOperators = digitsOperators()
            if (digitsOperators.isEmpty()) return ""

            val timesDivision = timesDivisionCalculate(digitsOperators)
            if (timesDivision.isEmpty()) return ""

            val result = addSubtractCalculate(timesDivision)
            return result.toString()
        }

        private fun addSubtractCalculate(passedList: MutableList<Any>): Float {
            var result = passedList[0] as Float

            for (i in passedList.indices) {
                if (passedList[i] is Char && i != passedList.lastIndex) {
                    val operator = passedList[i]
                    val nextDigit = passedList[i + 1] as Float
                    if (operator == '+')
                        result += nextDigit
                    if (operator == '-')
                        result -= nextDigit
                }
            }

            return result
        }

        private fun timesDivisionCalculate(passedList: MutableList<Any>): MutableList<Any> {
            var list = passedList
            while (list.contains('*') || list.contains('/')) {
                list = calcTimesDiv(list)
            }
            return list
        }

        private fun calcTimesDiv(passedList: MutableList<Any>): MutableList<Any> {
            val newList = mutableListOf<Any>()
            var restartIndex = passedList.size

            for (i in passedList.indices) {
                if (passedList[i] is Char && i != passedList.lastIndex && i < restartIndex) {
                    val operator = passedList[i]
                    val prevDigit = passedList[i - 1] as Float
                    val nextDigit = passedList[i + 1] as Float
                    when (operator) {
                        '*' -> {
                            newList.add(prevDigit * nextDigit)
                            restartIndex = i + 1
                        }
                        '/' -> {
                            newList.add(prevDigit / nextDigit)
                            restartIndex = i + 1
                        }
                        else -> {
                            newList.add(prevDigit)
                            newList.add(operator)
                        }
                    }
                }

                if (i > restartIndex)
                    newList.add(passedList[i])
            }

            return newList
        }


        private fun digitsOperators(): MutableList<Any> {
            val edtCalc = findViewById<TextView>(R.id.edtCalc)
            val dot = "."
            val list = mutableListOf<Any>()
            var currentDigit = ""
            for (character in edtCalc.text) {

                if (character.isDigit() || character.equals(dot)) {
                    currentDigit += character
                } else {
                    list.add(currentDigit.toFloat())
                    currentDigit = ""
                    list.add(character)
                }

            }

            if (currentDigit != "") {
                list.add(currentDigit.toFloat())
            }
            return list
        }

}








