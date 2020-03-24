package com.example.hw4

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData

class MovieItemRepository(private val movieDao: MovieItemDao){

    val allMovies: LiveData<List<Movie>> = movieDao.getAllMovies()

    @WorkerThread
    fun insert(movie: Movie){
        movieDao.insertMovie(movie)
    }

    @WorkerThread
    fun deleteAll(){
        movieDao.DeleteAll()
    }


}