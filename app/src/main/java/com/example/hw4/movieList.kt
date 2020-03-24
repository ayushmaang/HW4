package com.example.hw4

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SearchView
import android.widget.TextView
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.

LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import androidx.lifecycle.Observer

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [movieList.newInstance] factory method to
 * create an instance of this fragment.
 */
class movieList : Fragment(), SearchView.OnQueryTextListener{

    val adapter = MovieListAdapter()

    override fun onQueryTextChange(newText: String?): Boolean {

        return true
    }

    override fun onQueryTextSubmit(query: String?): Boolean {

        adapter.search(query)
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v =  inflater.inflate(R.layout.fragment_movie_list, container, false)

        val recyclerView = v.findViewById<RecyclerView>(R.id.move_list)

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        val model = ViewModelProviders.of(this).get(MovieViewModel::class.java)

        model.allMovies.observe(
            viewLifecycleOwner ,
            Observer<List<Movie>>{ movies ->
                movies?.let{
                    adapter.setMovies(it)
                }
            }
        )

        (v.findViewById<Button>(R.id.buttonRefresh)).setOnClickListener{
            model.refreshMovies(1)
            Log.i("Info", "Ran Refresh")
        }

        return v;
    }


    inner class MovieListAdapter():
        RecyclerView.Adapter<MovieListAdapter.MovieViewHolder>(){

        private var movies = emptyList<Movie>()
        private var moviesBackup= emptyList<Movie>()

        internal fun setMovies(movies: List<Movie>) {
            moviesBackup = movies
            this.movies = movies
            notifyDataSetChanged()
        }

        fun search(query: String?) {
            movies = movies.filter{it.title.contains(query!!)}
            notifyDataSetChanged()
        }

        fun restore(){
            movies = moviesBackup
            notifyDataSetChanged()
        }


        override fun getItemCount(): Int {
            return movies.size
        }


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
            val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.recyclerview_item, parent, false)
            return MovieViewHolder(v)
        }

        override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
            //holder.bindItems(movieList[position])

            Glide.with(this@movieList).load(R.string.picture_base_url.toString()+movies[position].poster_path).apply( RequestOptions().override(128, 128)).into(holder.view.findViewById(R.id.poster))

            holder.view.findViewById<TextView>(R.id.title).text=movies[position].title

            holder.view.findViewById<TextView>(R.id.rating).text=movies[position].vote_average.toString()


            holder.itemView.setOnClickListener(){


                // interact with the item

            }

        }

        inner class MovieViewHolder(val view: View): RecyclerView.ViewHolder(view), View.OnClickListener{
            override fun onClick(view: View?){
                if (view != null) {


                }
            }
        }
    }
}


