package com.example.moviesapp.ui.details

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.moviesapp.MvpApp
import com.example.moviesapp.R
import com.example.moviesapp.data.network.models.Person
import com.example.moviesapp.data.network.models.Profile
import com.example.moviesapp.data.network.models.Result
import com.example.moviesapp.ui.adapters.PhotosAdapter
import com.example.moviesapp.ui.base.BaseActivity
import com.example.moviesapp.ui.fullScreenFragment.FullScreenFragment
import com.example.moviesapp.utils.Constants
import com.example.moviesapp.utils.ItemClickListener
import kotlinx.android.synthetic.main.activity_details.*
import kotlinx.android.synthetic.main.content_details.*

class DetailsActivity : BaseActivity(), DetailsMvpView, ItemClickListener {


    var result: Result? = null
    private lateinit var presenter: DetailsPresenter<DetailsMvpView>

    private var adapter: PhotosAdapter? = null

    fun getStartIntent(context: Context): Intent {
        return Intent(context, DetailsActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        result = intent.extras.getParcelable(Constants().PERSON)
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.title = result!!.name
        }

        val dataManager = (applicationContext as MvpApp).getDataManager()
        presenter = DetailsPresenter(dataManager)
        presenter.onAttach(this)

        getData()

    }


    override fun getData() {
        presenter.getData(result!!.id)
        presenter.personImages(result!!.id)
    }

    override fun setImages(profiles: List<Profile>?) {
        val layoutManger = GridLayoutManager(this, 3)

        gallery_recycler.layoutManager = layoutManger
        adapter = PhotosAdapter(profiles, this)
        gallery_recycler.adapter = adapter

    }

    override fun setData(person: Person?) {
        val url = Constants().BASE_IMAGE_URL + person!!.profilePath
//        val uri = Uri.parse(url)
        person_image.setImageURI(url)
        birth_value.setText(person.birthday)
        known_value.setText(person.knownForDepartment)
        biography_value.setText(person.biography)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun OnItemClick(profile: Profile) {
        val image = Constants().BASE_IMAGE_URL + profile.filePath
        val fullScreenImageFragment = FullScreenFragment.newInstance(image)
        addFragment(fullScreenImageFragment)
    }

    private fun addFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(fragment_container.id, fragment)
            .addToBackStack(null)
            .commit()
    }

}
