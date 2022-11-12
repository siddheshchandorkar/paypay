package com.paypay.paypaytest

import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.paypay.common.utils.Constants
import com.paypay.network.RetrofitRepository
import com.paypay.paypaytest.presentation.ui.CurrencyActivity
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.MockitoAnnotations
import java.net.HttpURLConnection

class ApiTest {
    @Rule
    @JvmField
    val rule = ActivityScenarioRule(CurrencyActivity::class.java)

    private lateinit var mockWebServer: MockWebServer

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this).close()

        mockWebServer = MockWebServer()
        mockWebServer.start()

    }

    @Test
    fun checkCountryListJson(){
        val reader = MockResponseFileReader("country_list.json")
        assertNotNull(reader.content)
    }
    @Test
    fun checkCurrencyListJson(){
        val reader = MockResponseFileReader("currency_details.json")
        assertNotNull(reader.content)
    }

    @Test
    fun checkCountryListApi(){
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(MockResponseFileReader("country_list.json").content)
        mockWebServer.enqueue(response)

        // Act
        val  actualResponse = RetrofitRepository.instance.retrofitService.getAllCurrenciesName().execute()
        // Assert
        assertEquals(response.toString().contains("200"),actualResponse.code().toString().contains("200"))
    }
    @Test
    fun checkCurrencyListApi(){
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(MockResponseFileReader("currency_details.json").content)
        mockWebServer.enqueue(response)

        // Act
        val  actualResponse = RetrofitRepository.instance.retrofitService.getAllCurrenciesValue(Constants.API_KEY).execute()
        // Assert
        assertEquals(response.toString().contains("200"),actualResponse.code().toString().contains("200"))
    }

}