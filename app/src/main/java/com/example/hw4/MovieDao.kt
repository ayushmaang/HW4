package com.example.hw4

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MovieItemDao {

    @Query("SELECT * FROM movie_table order BY release_date DESC")
    fun getAllMovies(): LiveData<List<Movie>>


    @Query("DELETE FROM movie_table")
    fun DeleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovie(movie: Movie)


    @Query("SELECT * FROM movie_table order BY release_date ASC")
    fun getAllMoviesAsc(): LiveData<List<Movie>>


}