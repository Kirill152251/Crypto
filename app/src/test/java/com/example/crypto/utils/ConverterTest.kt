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

        assertThat(price.coinsPriceConverter(), `is`("0.000101"))
        assertThat(price1.coinsPriceConverter(), `is`("0.01"))
        assertThat(price2.coinsPriceConverter(), `is`("0.00321"))
    }
    @Test
    fun myConverter_priseIsMoreThan1andLessThenMillion_returnsNumberStringThatMoreThan1andLessThenMillion() {
        val price = 899999.3499
        val price1 = 1.7412934
        val price2 = 500_000.0

        assertThat(price.coinsPriceConverter(), `is`("899,999.35"))
        assertThat(price1.coinsPriceConverter(), `is`("1.74"))
        assertThat(price2.coinsPriceConverter(), `is`("500,000"))
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

        assertThat(price.coinsPriceConverter(), `is`("1.1M"))
        assertThat(price1.coinsPriceConverter(), `is`("32.3M"))
        assertThat(price2.coinsPriceConverter(), `is`("999.3M"))
        assertThat(price3.coinsPriceConverter(), `is`("2.9B"))
        assertThat(price4.coinsPriceConverter(), `is`("13.1B"))
        assertThat(price5.coinsPriceConverter(), `is`("999.1B"))
        assertThat(price6.coinsPriceConverter(), `is`("1.9T"))
        assertThat(price7.coinsPriceConverter(), `is`("50.7T"))
        assertThat(price8.coinsPriceConverter(), `is`("846.1T"))
    }
}