package com.widyatama.moviedirectory.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import com.widyatama.moviedirectory.R
import com.widyatama.moviedirectory.adapter.recyler.FavoriteListAdapter
import com.widyatama.moviedirectory.adapter.recyler.MovieListAdapter
import com.widyatama.moviedirectory.core.db.model.FavoriteMovie
import com.widyatama.moviedirectory.core.model.movie.Movie
import com.widyatama.moviedirectory.core.model.movie.MovieResponse
import com.widyatama.moviedirectory.core.model.movie.Result
import com.widyatama.moviedirectory.core.presenter.FavoritePresenter
import com.widyatama.moviedirectory.core.presenter.MoviePresenter
import com.widyatama.moviedirectory.core.view.FavoriteView
import com.widyatama.moviedirectory.core.view.MovieView
import org.jetbrains.anko.support.v4.ctx
import org.jetbrains.anko.support.v4.toast
import java.util.*

class MovieListFragment : Fragment(), MovieView, FavoriteView {


    private var recyclerView: RecyclerView? = null
    private var progressBar: ProgressBar? = null
    private var swipeRefreshLayout: SwipeRefreshLayout? = null

    private var movies: MutableList<Result> = ArrayList()
    private var favoriteMovies: ArrayList<FavoriteMovie>? = ArrayList()

    private var position: Int = 0
    private var moviePresenter: MoviePresenter? = null
    private var favoritePresenter: FavoritePresenter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_movie_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.movieFragmentRecyclerView)
        progressBar = view.findViewById(R.id.movieFragmentProgressBar)
        swipeRefreshLayout = view.findViewById(R.id.movieFragmentSwipeRefreshLayout)
        swipeRefreshLayout?.setColorSchemeColors(
                ContextCompat.getColor(ctx, R.color.colorAccent),
                ContextCompat.getColor(ctx, R.color.colorPrimary),
                ContextCompat.getColor(ctx, R.color.colorPrimaryDark))

        moviePresenter = MoviePresenter(this)
        favoritePresenter = FavoritePresenter(ctx, this)
        position = if (arguments != null) arguments!!.getInt(POSITION, 0) else 0
        movies = ArrayList()
        favoriteMovies = ArrayList()
        when (position) {
            0 -> moviePresenter?.getNowPlayingMovies(1)
            1 -> moviePresenter?.getUpcomingMovies(1)
            else -> favoritePresenter?.showFavoriteData()
        }

        swipeRefreshLayout!!.setOnRefreshListener {
            when (position) {
                0 -> moviePresenter?.getNowPlayingMovies(1)
                1 -> moviePresenter?.getUpcomingMovies(1)
                else -> favoritePresenter?.showFavoriteData()
            }
        }
    }

    override fun showMovieLoading() {
        if (!swipeRefreshLayout!!.isRefreshing) {
            progressBar?.visibility = View.VISIBLE
        }

        recyclerView?.visibility = View.INVISIBLE
    }

    override fun hideMovieLoading() {
        recyclerView?.visibility = View.VISIBLE

        if (!swipeRefreshLayout!!.isRefreshing) {
            progressBar?.visibility = View.INVISIBLE
        } else {
            swipeRefreshLayout?.isRefreshing = false
        }
    }

    override fun showMovieError(message: String) {
        toast(message)
        if (swipeRefreshLayout!!.isRefreshing) {
            swipeRefreshLayout?.isRefreshing = false
        }
    }

    override fun showMovies(data: MovieResponse) {
        movies.clear()
        movies.addAll(data.results!!)
        val movieListAdapter = MovieListAdapter(ctx, movies)
        recyclerView?.layoutManager = LinearLayoutManager(ctx, LinearLayout.VERTICAL, false)
        recyclerView?.adapter = movieListAdapter
    }

    override fun showMovie(data: Movie) {}
    override fun onAdded(message: String) {}
    override fun onDeleted(message: String) {}

    override fun showFavoriteData(data: ArrayList<FavoriteMovie>) {
        favoriteMovies?.clear()
        favoriteMovies?.addAll(data)
        val favoriteAdapter = FavoriteListAdapter(ctx, favoriteMovies!!)
        recyclerView?.layoutManager = LinearLayoutManager(ctx, LinearLayout.VERTICAL, false)
        recyclerView?.adapter = favoriteAdapter
        swipeRefreshLayout?.isRefreshing = false
    }

    companion object {

        const val POSITION = "position"

        fun newInstance(position: Int): MovieListFragment {
            val movieListFragment = MovieListFragment()

            val args = Bundle()
            args.putInt(POSITION, position)
            movieListFragment.arguments = args

            return movieListFragment
        }
    }
}
