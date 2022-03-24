package com.example.crypto.utils


import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.Test

class ConverterTest {

    @Test
    fun myConverter_priseIsLessThan1_returnsNumberStringThatLessThen0() {
        val price = 0.000101
        val price1 = 0.01
        val price2 = 0.00321000001

        val result = myConverter(price)
        val result1 = myConverter(price1)
        val result2 = myConverter(price2)

        assertThat(result, `is`("0.000101"))
        assertThat(result1, `is`("0.01"))
        assertThat(result2, `is`("0.00321"))
    }
    @Test
    fun myConverter_priseIsMoreThan1andLessThenMillion_returnsNumberStringThatMoreThan1andLessThenMillion() {
        val price = 899999.3499
        val price1 = 1.7412934
        val price2 = 500_000.0

        val result = myConverter(price)
        val result1 = myConverter(price1)
        val result2 = myConverter(price2)

        assertThat(result, `is`("899,999.35"))
        assertThat(result1, `is`("1.74"))
        assertThat(result2, `is`("500,000"))
    }
    @Test
    fun myConverter_priseIsMoreThanMillion_returnsNumberStringThatMoreMillion() {
        val price = 1_100_324.0
        val price1 = 32_344_324.342
        val price2 = 999_324_829.123
        val price3 = 2_908_324_829.123
        val price4 = 13_136_324_829.123
        val price5 = 999_100_324_829.0
        val price6 = 1_909_100_324_829.0
        val price7 = 50_726_079_737_824.0
        val price8 = 846_139_100_324_829.0

        val result = myConverter(price)
        val result1 = myConverter(price1)
        val result2 = myConverter(price2)
        val result3 = myConverter(price3)
        val result4 = myConverter(price4)
        val result5 = myConverter(price5)
        val result6 = myConverter(price6)
        val result7 = myConverter(price7)
        val result8 = myConverter(price8)

        assertThat(result, `is`("1.1M"))
        assertThat(result1, `is`("32.3M"))
        assertThat(result2, `is`("999.3M"))
        assertThat(result3, `is`("2.9B"))
        assertThat(result4, `is`("13.1B"))
        assertThat(result5, `is`("999.1B"))
        assertThat(result6, `is`("1.9T"))
        assertThat(result7, `is`("50.7T"))
        assertThat(result8, `is`("846.1T"))
    }
}