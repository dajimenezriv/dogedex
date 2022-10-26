package com.dajimenezriv.dogedex.di.example

class TestClass {
    fun testDataDownloadedUpdatesState() {
        // val goodRepository = GoodRepository()
        // val viewModel = SomeViewModel(goodRepository)
        val fakeRepository = FakeRepository()
        val viewModel = SomeViewModel(fakeRepository)
        viewModel.downloadData()
        assert(viewModel.data == "Real data form server")
    }
}