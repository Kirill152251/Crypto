package com.example.crypto.utils


import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.Test

class ConverterTest {

    @Test
    fun `Converter when prise is less than 1 returns string that less then 1`() {
        val price = 0.000101
        val price1 = 0.01
        val price2 = 0.00321000001

        assertThat(price.priceConverter(), `is`("0.000101"))
        assertThat(price1.priceConverter(), `is`("0.01"))
        assertThat(price2.priceConverter(), `is`("0.00321"))
    }
    @Test
    fun `Converter when prise is more than 1 and less then million return String that more than 1 and less then million`() {
        val price = 899999.3499
        val price1 = 1.7412934
        val price2 = 500_000.0

        assertThat(price.priceConverter(), `is`("899,999.35"))
        assertThat(price1.priceConverter(), `is`("1.74"))
        assertThat(price2.priceConverter(), `is`("500,000"))
    }
    @Test
    fun `Converter when prise is more than million return string that more million`() {
        val price = 1_100_324.0
        val price1 = 32_344_324.342
        val price2 = 999_324_829.123
        val price3 = 2_908_324_829.123
        val price4 = 13_136_324_829.123
        val price5 = 999_100_324_829.0
        val price6 = 1_909_100_324_829.0
        val price7 = 50_726_079_737_824.0
        val price8 = 846_139_100_324_829.0

        assertThat(price.priceConverter(), `is`("1.1M"))
        assertThat(price1.priceConverter(), `is`("32.3M"))
        assertThat(price2.priceConverter(), `is`("999.3M"))
        assertThat(price3.priceConverter(), `is`("2.9B"))
        assertThat(price4.priceConverter(), `is`("13.1B"))
        assertThat(price5.priceConverter(), `is`("999.1B"))
        assertThat(price6.priceConverter(), `is`("1.9T"))
        assertThat(price7.priceConverter(), `is`("50.7T"))
        assertThat(price8.priceConverter(), `is`("846.1T"))
    }
}