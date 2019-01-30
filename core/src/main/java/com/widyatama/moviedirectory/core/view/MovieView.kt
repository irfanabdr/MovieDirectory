package com.widyatama.moviedirectory.core.view

import com.widyatama.moviedirectory.core.model.movie.Movie
import com.widyatama.moviedirectory.core.model.movie.MovieResponse

interface MovieView {
    fun showMovieLoading()
    fun hideMovieLoading()
    fun showMovieError(message: String)
    fun showMovies(data: MovieResponse)
    fun showMovie(data: Movie)
}
