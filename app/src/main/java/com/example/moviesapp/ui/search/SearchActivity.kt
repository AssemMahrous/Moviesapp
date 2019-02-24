package com.example.moviesapp.ui.search

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.SearchView
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
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.content_search.*
import java.util.*

class SearchActivity : BaseActivity(), SearchMvpView {

    private lateinit var presenter: SearchPresenter<SearchMvpView>
    private lateinit var searchView: SearchView
    private var result: ArrayList<Result?> = ArrayList()
    private var adapter: PeopleAdapter? = null
    private var page: Int = 1
    private var loading: Boolean = false
    fun getStartIntent(context: Context): Intent {
        return Intent(context, SearchActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        setSupportActionBar(toolbar)

        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        val dataManager = (applicationContext as MvpApp).getDataManager()
        presenter = SearchPresenter(dataManager)
        presenter.onAttach(this)

        setViews()
    }


    override fun displayResult(results: List<Result?>) {
        ClearSearch()
        addResults(results)
    }

    override fun displayError(s: String) {
        Toast.makeText(this,s,Toast.LENGTH_LONG).show()
    }

    override fun concatResult(results: List<Result?>) {
        if (this.result.size > 0) {
            this.result.removeAt(this.result.size - 1)
            adapter?.notifyItemRemoved(this.result.size)
        }

        addResults(results)
        loading = false
    }

    override fun incrementPagination() {
        page++
    }

    override fun stopPagination() {
        page=0
    }

    override fun initializePagination() {
        page=1
    }

    override fun goToPerson(it: Result?) {
        val intent = DetailsActivity().getStartIntent(this)
        intent.putExtra(Constants().PERSON, it)
        startActivity(intent)
    }

    override fun setViews() {
        val layoutManger = GridLayoutManager(this, 2)
        person_search.layoutManager = layoutManger
        adapter = PeopleAdapter(result) {
            presenter.itemClick(it)
        }

        person_search.adapter = adapter

        person_search.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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

    private fun getData() {
        presenter.getDataPaginated(searchView.query.toString(), page)
    }

    private fun addResults(results: List<Result?>) {
        this.result.addAll(results)
        notifyChange()
    }

    private fun ClearSearch() {
        this.result.clear()
        notifyChange()
    }

    private fun notifyChange() {
        adapter?.notifyDataSetChanged()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView = menu.findItem(R.id.action_search)
            .actionView as SearchView
        searchView.setSearchableInfo(
            searchManager
                .getSearchableInfo(componentName)
        )
        searchView.setMaxWidth(Integer.MAX_VALUE)
        searchView.setQueryHint(resources.getString(R.string.search_people))
        presenter.getResultsQuery(searchView)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_search) {
            return true
        }
        if (id == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onDestroy() {
        super.onDestroy()
        presenter.destroy()
    }

}
