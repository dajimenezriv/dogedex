package com.dajimenezriv.dogedex.doglist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.dajimenezriv.dogedex.Dog
import com.dajimenezriv.dogedex.databinding.ActivityDogListBinding
import com.dajimenezriv.dogedex.dogdetail.DogDetailActivity
import com.dajimenezriv.dogedex.dogdetail.DogDetailActivity.Companion.DOG_KEY

class DogListActivity : AppCompatActivity() {
    // this is the way to instantiate a view model
    // instead of dogListViewModel = DogListViewModel()
    private val dogListViewModel: DogListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityDogListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val recycler = binding.dogRecycler
        val adapter = DogAdapter()

        adapter.setOnItemClickListener {
            // send dog to DogDetailActivity
            val intent = Intent(this, DogDetailActivity::class.java)
            intent.putExtra(DOG_KEY, it)
            startActivity(intent)
        }

        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter

        dogListViewModel.dogs.observe(this, Observer<List<Dog>>{ dogs ->
            adapter.submitList(dogs)
        })
    }
}
