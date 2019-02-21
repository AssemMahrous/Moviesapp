package com.example.moviesapp.ui.main

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesapp.MvpApp
import com.example.moviesapp.R
import com.example.moviesapp.data.network.models.Result
import com.example.moviesapp.ui.adapters.PeopleAdapter
import com.example.moviesapp.ui.base.BaseActivity
import com.example.moviesapp.ui.details.DetailsActivity
import com.example.moviesapp.utils.Constants
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.util.*

class MainActivity : BaseActivity(), MainMvpView {


    private lateinit var presenter: MainPresenter<MainMvpView>
    private var result: ArrayList<Result?> = ArrayList()
    private var adapter: PeopleAdapter? = null
    private var page: Int = 1
    private var loading: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        val dataManager = (applicationContext as MvpApp).getDataManager()
        presenter = MainPresenter(dataManager)
        presenter.onAttach(this)

        initRecyclerView()
        getData()

        search_bar.setOnClickListener { presenter.searchClick() }
    }


    override fun initRecyclerView() {
        val layoutManger = GridLayoutManager(this, 2)
        popular_people_list.layoutManager = layoutManger
        adapter = PeopleAdapter(result) {
            presenter.itemClick(it)
        }

        popular_people_list.adapter = adapter

        popular_people_list.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (loading || page == 0 || result.size == 0)
                    return
                val totalItemCount = layoutManger.getItemCount()
                val lastVisibleItem =
                    (Objects.requireNonNull<RecyclerView.LayoutManager>(recyclerView.getLayoutManager()) as LinearLayoutManager)
                        .findLastVisibleItemPosition()
                if (lastVisibleItem == totalItemCount - 1) {
                    loading = true
                    initLoading()
                }

            }
        })

    }

    private fun initLoading() {
        result.add(null)
        adapter?.notifyItemInserted(result.size - 1)
        getData()
    }

    override fun setData(results: List<Result?>) {
        if (result.size > 0) {
            result.removeAt(results.size - 1)
            adapter?.notifyItemRemoved(results.size)
        }
        result.addAll(results)
        adapter!!.notifyDataSetChanged()
        loading = false

    }

    override fun getData() {
        presenter.getData(page)
    }

    override fun endPagination() {
        page = 0
    }

    override fun incrementPagination() {
        page++
    }

    override fun goToPerson(it: Result?) {
        val intent = DetailsActivity().getStartIntent(this)
        intent.putExtra(Constants().PERSON, it)
        startActivity(intent)
    }

    override fun goToSearch() {
        Toast.makeText(this, R.string.toast_search, Toast.LENGTH_LONG).show()
    }

}
