package com.example.hw4
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class MovieViewModel(application: Application): AndroidViewModel(application){

    private var parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob+ Dispatchers.Main


    private val scope = CoroutineScope(coroutineContext)


    private var disposable: Disposable? = null


    private val repository: MovieItemRepository = MovieItemRepository(MovieRoomDatabase.getDatabase(application).movieDao())

    val allMovies: LiveData<List<Movie>>

    init {
        allMovies = repository.allMovies
    }


    fun refreshMovies(page: Int){

        //TODO: add your API key from themoviedb.org
        disposable =
            RetrofitService.create("https://api.themoviedb.org/3/").getNowPlaying("c04b445a825b21e81d6858d8922a4e0b",page).subscribeOn(
                Schedulers.io()).observeOn(
                AndroidSchedulers.mainThread()).subscribe(
                {result -> showResult(result)},
                {error -> showError(error)})

        Log.i("Info", disposable.toString())
    }



    private fun showError(error: Throwable?) {
        Log.e("rip", error.toString());
        //TODO: handle error
    }

    private fun showResult(movies: Movies?) {
        deleteAll()
        movies?.results?.forEach { movie ->
            insert(movie)
        }
    }

    private fun insert(movie: Movie) = scope.launch(Dispatchers.IO) {
        repository.insert(movie)
    }

    private fun deleteAll() = scope.launch (Dispatchers.IO){
        repository.deleteAll()
    }




}