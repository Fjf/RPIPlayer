package com.example.rpiplayer

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }
}

class UrlFilterTest {

    @Test
    fun testFilterTextBeforeUrl() {
        val input1 = "shutdown https://example.com"
        val expected1 = "https://example.com"
        val result1 = filterTextBeforeUrl(input1)
        println("Test 1: Input = $input1, Expected = $expected1, Result = $result1")
        assertEquals(expected1, result1)

        val input2 = "https://example.com more text"
        val expected2 = "https://example.com"
        val result2 = filterTextBeforeUrl(input2)
        println("Test 2: Input = $input2, Expected = $expected2, Result = $result2")
        assertEquals(expected2, result2)

        val input3 = "no url here"
        val expected3 = "no url here"
        val result3 = filterTextBeforeUrl(input3)
        println("Test 3: Input = $input3, Expected = $expected3, Result = $result3")
        assertEquals(expected3, result3)

        val input4 = "https://example.com?query=param"
        val expected4 = "https://example.com?query=param"
        val result4 = filterTextBeforeUrl(input4)
        println("Test 4: Input = $input4, Expected = $expected4, Result = $result4")
        assertEquals(expected4, result4)
    }
}