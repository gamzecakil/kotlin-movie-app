package com.gamzeuysal.kotlinmovieapp.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.ArrayList

//data sınıfı api den veri
data class Root(@SerializedName("resultCount")
                val resultCount:Int ,
                @SerializedName("results") val results:ArrayList<ResultModel>){
}

data class ResultModel(@SerializedName("wrapperType") val wrapperType:String,
                       @SerializedName("kind") val kind:String,
                       @SerializedName("trackName") val trackName :String,
                       @SerializedName("previewUrl") val previewUrl:String,
                       @SerializedName("artistName") val artistName:String,
                       @SerializedName("collectionName") val  collectionName: String,
                       @SerializedName("artworkUrl100") val artworkUrl100:String ):Serializable{
}
