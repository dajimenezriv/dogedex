package com.dajimenezriv.dogedex.di.example

class FakeRepository: RepositoryInterface {
    override fun downloadData(): String {
        val data = "Fake data to test"
        return data
    }
}
