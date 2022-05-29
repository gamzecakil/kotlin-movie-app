package com.gamzeuysal.kotlinmovieapp.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gamzeuysal.kotlinmovieapp.R
import com.gamzeuysal.kotlinmovieapp.model.ResultModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.row_layout.view.*

class RecylerViewAdapter(private val resultModel: ArrayList<ResultModel>, val listener: Listener) :
    RecyclerView.Adapter<RecylerViewAdapter.RowHolder>() {
    interface Listener {
        fun onItemClick(root: ResultModel)
    }

    class RowHolder(view: View) : RecyclerView.ViewHolder(view) {
        private var TAG: String = "RecylerViewAdapter"

        fun bind(resultModel: ResultModel, position: Int, listener: Listener) {

            Log.d(TAG, resultModel.toString())
            if (!resultModel.artistName.isNullOrBlank() && resultModel.artistName.length > 10) {
                itemView.artistName.text = resultModel.artistName.substring(0, 9) + "...";
            } else {
                itemView.artistName.text = resultModel.artistName;
            }



            if (!resultModel.collectionName.isNullOrBlank() && resultModel.collectionName.length > 20)
                itemView.collectionName.text = resultModel.collectionName.substring(0, 19) + "..."
            else
                itemView.collectionName.text = resultModel.collectionName

            Picasso.get().load(resultModel.artworkUrl100).into(itemView.imageView)
            itemView.cardView.setOnClickListener {
                listener.onItemClick(resultModel)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RowHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_layout, parent, false)
        return RowHolder(view)
    }

    override fun onBindViewHolder(holder: RowHolder, position: Int) {

        holder.bind(resultModel[position], position, listener)

    }

    override fun getItemCount(): Int {
        return resultModel.count()
    }
}