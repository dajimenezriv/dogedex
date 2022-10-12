package com.dajimenezriv.dogedex.doglist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.dajimenezriv.dogedex.models.Dog
import com.dajimenezriv.dogedex.api.APIResponseStatus
import com.dajimenezriv.dogedex.databinding.ActivityDogListBinding
import com.dajimenezriv.dogedex.dogdetail.DogDetailActivity
import com.dajimenezriv.dogedex.dogdetail.DogDetailActivity.Companion.DOG_KEY

private const val GRID_SPAN_COUNT = 3

class DogListActivity : AppCompatActivity() {
    // this is the way to instantiate a view model
    // instead of dogListViewModel = DogListViewModel()
    private val viewModel: DogListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityDogListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val loadingWheel = binding.loadingWheel

        val recycler = binding.dogRecycler
        val adapter = DogAdapter()

        adapter.setOnItemClickListener {
            // send dog to DogDetailActivity
            val intent = Intent(this, DogDetailActivity::class.java)
            intent.putExtra(DOG_KEY, it)
            startActivity(intent)
        }

        recycler.layoutManager = GridLayoutManager(this, GRID_SPAN_COUNT)
        recycler.adapter = adapter

        viewModel.dogs.observe(this, Observer<List<Dog>> { dogs ->
            adapter.submitList(dogs)
        })

        viewModel.status.observe(this) { status ->
            when (status) {
                is APIResponseStatus.Error -> {
                    loadingWheel.visibility = View.GONE
                    Toast.makeText(this, status.messageId, Toast.LENGTH_SHORT).show()
                }
                is APIResponseStatus.Loading -> {
                    // show progress bar
                    loadingWheel.visibility = View.VISIBLE
                }
                is APIResponseStatus.Success -> {
                    // hide progress bar
                    loadingWheel.visibility = View.GONE
                }
            }
        }
    }
}
