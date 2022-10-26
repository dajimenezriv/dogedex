package com.dajimenezriv.dogedex.di.example

class GoodRepository: RepositoryInterface {
    override fun downloadData(): String {
        val data = "Real data form server"
        return data
    }
}