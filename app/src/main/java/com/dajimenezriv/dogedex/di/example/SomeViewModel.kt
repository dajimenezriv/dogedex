package com.dajimenezriv.dogedex.di.example

class SomeViewModel(
    private val repository: RepositoryInterface
) {
    var data = ""

    fun downloadData() {
        data = repository.downloadData()
    }
}