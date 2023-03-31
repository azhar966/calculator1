package com.zebdul.calculator1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.zebdul.calculator1.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var canAddOperation = false
    private var canAddDecimal = true
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun numberAction(view: View) {
        if (view is Button) {
            if (view.text == ".") {
                if (canAddDecimal) binding.workingTv.append(view.text)
                canAddDecimal = false
            } else binding.workingTv.append(view.text)
            canAddOperation = true
        }
    }

    fun operationAction(view: View) {
        if (view is Button && canAddOperation) {
            binding.workingTv.append(view.text)
            canAddOperation = false
        }
    }

    fun allClearAction(view: View) {
        binding.workingTv.text = ""
        binding.resultTv.text = ""

    }

    fun backSpaceAction(view: View) {
        val length = binding.workingTv.length()
        if (length > 0) {
            binding.workingTv.text = binding.workingTv.text.subSequence(0, length - 1)
        }
    }

    fun equalsAction(view: View) {
        binding.resultTv.text = calculateResult()
    }

    private fun calculateResult(): String {
        val digitsOperators = digitsOperators()
        if (digitsOperators.isEmpty()) return ""

        val timeDivision = timesDivisionCalculate(digitsOperators)
        if (timeDivision.isEmpty()) return ""

        val result = addSubtractCalculate(timeDivision)
        return result.toString()

    }

    private fun addSubtractCalculate(passedList: MutableList<Any>): Float {
        var result = passedList[0] as Float

        for (i in passedList.indices) {
            if (passedList[i] is Char && i != passedList.lastIndex) {
                val operator = passedList[i]
                val nextDigit = passedList[i + 1] as Float
                if (operator == '+') result += nextDigit
                if (operator == '-') result -= nextDigit
            }
        }

        return result

    }

    private fun timesDivisionCalculate(passedList: MutableList<Any>): MutableList<Any> {
        var list = passedList
        while (list.contains('x') || list.contains('/')) {
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
                    'x' -> {
                        newList.add(prevDigit + nextDigit)
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
            if (i > restartIndex) newList.add(passedList[i])
        }
        return newList

    }

    private fun digitsOperators(): MutableList<Any> {
        val list = mutableListOf<Any>()
        var currentDigit = ""
        for (character in binding.workingTv.text) {
            if (character.isDigit() || character == '.') currentDigit += character
            else {
                list.add(currentDigit.toFloat())
                currentDigit = ""
                list.add(character)
            }
        }
        if (currentDigit != "") list.add(currentDigit.toFloat())
        return list
    }
}