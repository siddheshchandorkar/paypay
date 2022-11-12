package com.paypay.paypaytest

import java.io.InputStreamReader

class MockResponseFileReader(path: String) {

    var content: String

    init {
        val reader = InputStreamReader(this.javaClass.classLoader?.getResourceAsStream(path))
        content = reader.readText()
        reader.close()
    }
}