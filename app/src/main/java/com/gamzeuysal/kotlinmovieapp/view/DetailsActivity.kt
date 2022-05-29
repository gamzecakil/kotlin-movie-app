package com.gamzeuysal.kotlinmovieapp.view

import android.annotation.SuppressLint
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.ImageView
import android.widget.MediaController
import android.widget.TextView
import android.widget.VideoView
import com.gamzeuysal.kotlinmovieapp.R
import com.gamzeuysal.kotlinmovieapp.model.ResultModel
import com.google.gson.JsonSerializer
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.row_layout.view.*
import java.util.logging.Level.parse

class DetailsActivity : AppCompatActivity() {

    var wrapperType:TextView? = null
    var kind:TextView? = null
    var artistName:TextView? = null
    var collectionName:TextView? = null
    var trackName:TextView? = null
    var videoView:VideoView?=null
    var imageViewDetail :ImageView?=null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        imageViewDetail = findViewById(R.id.imageViewDetail)
        wrapperType = findViewById(R.id.wrappperTypeDetail)
        kind= findViewById(R.id.kindDetail)
        artistName = findViewById(R.id.artistNameDetail)
        collectionName = findViewById(R.id.collectionNameDetails)
        trackName = findViewById(R.id.trackNameDetail)
        videoView = findViewById(R.id.videoView)


        val resultModel=intent.getSerializableExtra("resultModel") as ResultModel
    

        Picasso.get().load(resultModel.artworkUrl100).into(imageViewDetail)
        if(resultModel.wrapperType.isNullOrEmpty())
            wrapperType?.setText("Wrapper Type    :"+"Not Exists")
        else
        wrapperType?.setText("Wrapper Type    :"+resultModel.wrapperType)

        if(resultModel.kind.isNullOrEmpty())
            kind?.setText("Kind            :"+"Not Exists")
        else
            kind?.setText("Kind            :"+resultModel.kind)

        if(resultModel.trackName.isNullOrEmpty())
            trackName?.setText("Track Name      :"+"Not Exists")
        else
            trackName?.setText("Track Name      :"+resultModel.trackName)

        if(resultModel.artistName.isNullOrEmpty())
            artistName?.setText("Artist Name     :"+"Not Exists")
        else
            artistName?.setText("Artist Name     :"+resultModel.artistName)

        if(resultModel.collectionName.isNullOrEmpty())
            collectionName?.setText("Collection Name :"+"Not Exists")
        else
            collectionName?.setText("Collection Name :"+resultModel.collectionName)

        if(!resultModel.previewUrl.isNullOrEmpty())
        {
           val mediaController = MediaController(this)
            mediaController.setAnchorView(videoView)
            val uri: Uri = Uri.parse(resultModel.previewUrl)
            videoView?.setMediaController(mediaController)
            videoView?.setVideoURI(uri)
            videoView?.requestFocus()
            videoView?.start()
        }

//ResultModel(wrapperType=track, kind=song, trackName=Rondo Acapricio, previewUrl=https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview115/v4/90/e3/ba/90e3ba53-756c-6533-db7e-620141232b49/mzaf_2624005745719199578.plus.aac.p.m4a, artistName=Tosca, collectionName=J.A.C., artworkUrl100=https://is4-ssl.mzstatic.com/image/thumb/Music124/v4/50/b4/25/50b425e4-3f38-a658-5a5e-67b653bd5863/s05.vdahaxja.jpg/100x100bb.jpg)
//ResultModel(wrapperType=track, kind=song, trackName=Rondo Acapricio, previewUrl=https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview115/v4/90/e3/ba/90e3ba53-756c-6533-db7e-620141232b49/mzaf_2624005745719199578.plus.aac.p.m4a, artistName=Tosca, collectionName=J.A.C., artworkUrl100=https://is4-ssl.mzstatic.com/image/thumb/Music124/v4/50/b4/25/50b425e4-3f38-a658-5a5e-67b653bd5863/s05.vdahaxja.jpg/100x100bb.jpg)

    }
}