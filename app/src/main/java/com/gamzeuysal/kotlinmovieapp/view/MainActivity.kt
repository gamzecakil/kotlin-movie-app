package com.gamzeuysal.kotlinmovieapp.view

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.GridLayoutManager
import com.gamzeuysal.kotlinmovieapp.R
import com.gamzeuysal.kotlinmovieapp.adapter.RecylerViewAdapter
import com.gamzeuysal.kotlinmovieapp.model.ResultModel
import com.gamzeuysal.kotlinmovieapp.model.Root
import com.gamzeuysal.kotlinmovieapp.service.ITunesApi
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import android.view.View;
import android.widget.*


/*
    - Program ilk açıldığında search bar kontrolüne  eleman girilir.
    - Her eleman girilmesinde TextListener tetiklenerek api ye search bar girilen veriye göre sorgu atalır.
    - Eğer wrapperType butonlarından birisi tıklanmıssa tıklanan wrapperType göre RecyclerView ögesi tekrar doldurulur.
    - Eğer wrapperType butonlarından hiç biri seçilmeden arama yapılmıssa sadece search bar  kontrolüne girirlen veriye göre arama sonuçlarını RecyclerView de gösterilir.
    - Api sorgusunda veriler  her sayfada 10 veri olacak şekilde çekilir.
    - ScrollView sayfanın sonuna gelince sonraki sayfadan 10 tane veri getirir.
    - Listelenen verilerden herhanngi birine tıklanarak details sayfasına gidilerek diğer özelikleri görülebilir.
*/

class MainActivity : AppCompatActivity(), RecylerViewAdapter.Listener {

    private var TAG: String = "Main"

    var btnTrack: Button? = null
    var btnaudioBook: Button? = null
    var btnArtist : Button? = null
    var btnCollection :Button? = null

    var searchText: String? = null


    var offset = 0  //offset --> page
    var limit: Int = 10 //limit



    private val BASE_URL = "https://itunes.apple.com/"
    val listModel = ArrayList<ResultModel>()
    var tempListModel=ArrayList<ResultModel>()

    private var recyclerViewAdapter: RecylerViewAdapter? = null

    //Disposable
    private var compositeDisposable: CompositeDisposable? = null


    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        btnTrack = findViewById(R.id.buttonTrack)
        btnaudioBook = findViewById(R.id.buttonaudioBook)
        btnArtist = findViewById(R.id.buttonArtist)
        btnCollection = findViewById(R.id.buttonCollection)

 searchView.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
     override fun onQueryTextSubmit(p0: String?): Boolean {
         searchView.clearFocus()
         return false
     }

     override fun onQueryTextChange(search: String?): Boolean {
         listModel.clear()
         searchText=search
         offset=0
         loadData(offset,limit)
         return false
     }
 })

        btnTrack?.setOnClickListener{
            Log.d(TAG, "TRACK button clicked.")
            btnTrack?.setBackgroundColor(Color.BLACK)
            btnTrack?.setTextColor(Color.WHITE)
            btnCollection?.setBackgroundColor(Color.WHITE)
            btnCollection?.setTextColor(Color.BLACK)
            btnaudioBook?.setBackgroundColor(Color.WHITE)
            btnaudioBook?.setTextColor(Color.BLACK)
            btnArtist?.setBackgroundColor(Color.WHITE)
            btnArtist?.setTextColor(Color.BLACK)
           tempListModel =ArrayList(listModel.filter { x->x.wrapperType=="track" })
            filterForWrapper(tempListModel)
        }
        btnaudioBook?.setOnClickListener{
            Log.d(TAG, "ARTIST button clicked.")
            btnaudioBook?.setBackgroundColor(Color.BLACK)
            btnaudioBook?.setTextColor(Color.WHITE)
            btnCollection?.setBackgroundColor(Color.WHITE)
            btnCollection?.setTextColor(Color.BLACK)
            btnTrack?.setBackgroundColor(Color.WHITE)
            btnTrack?.setTextColor(Color.BLACK)
            btnArtist?.setBackgroundColor(Color.WHITE)
            btnArtist?.setTextColor(Color.BLACK)
            tempListModel =ArrayList(listModel.filter { x->x.wrapperType=="audiobook" })
            filterForWrapper(tempListModel)
        }
        btnArtist?.setOnClickListener{
            Log.d(TAG, "ARTIST button clicked.")
            btnArtist?.setBackgroundColor(Color.BLACK)
            btnArtist?.setTextColor(Color.WHITE)
            btnCollection?.setBackgroundColor(Color.WHITE)
            btnCollection?.setTextColor(Color.BLACK)
            btnTrack?.setBackgroundColor(Color.WHITE)
            btnTrack?.setTextColor(Color.BLACK)
            btnaudioBook?.setBackgroundColor(Color.WHITE)
            btnaudioBook?.setTextColor(Color.BLACK)
            tempListModel =ArrayList(listModel.filter { x->x.wrapperType=="collection" })
            filterForWrapper(tempListModel)
        }
        btnCollection?.setOnClickListener{
            Log.d(TAG, "COLLECTION button clicked.")
            btnCollection?.setBackgroundColor(Color.BLACK)
            btnCollection?.setTextColor(Color.WHITE)
            btnTrack?.setBackgroundColor(Color.WHITE)
            btnTrack?.setTextColor(Color.BLACK)
            btnaudioBook?.setBackgroundColor(Color.WHITE)
            btnaudioBook?.setTextColor(Color.BLACK)
            btnArtist?.setBackgroundColor(Color.WHITE)
            btnArtist?.setTextColor(Color.BLACK)
            tempListModel =ArrayList(listModel.filter { x->x.wrapperType=="collection" })
            filterForWrapper(tempListModel)
        }




        recyclerView.layoutManager = GridLayoutManager(applicationContext, 3)


        //pagination
        nestedScrollView.setOnScrollChangeListener { v: NestedScrollView?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->
            val nestedScrollView = checkNotNull(v) {
                return@setOnScrollChangeListener
            }
            val lastChild = nestedScrollView.getChildAt(nestedScrollView.childCount - 1)
            if (lastChild != null) {
                if ((scrollY >= (lastChild.measuredHeight - nestedScrollView.measuredHeight) && scrollY > oldScrollY)) {
                    offset++;
                    progressBar.setVisibility(View.VISIBLE);

                    loadData( offset, limit);
                }
            }

        }

        compositeDisposable = CompositeDisposable()

    }

    private fun loadData(offset: Int, limit: Int) {
        if (offset > 20) {
            Toast.makeText(this, "Sayfa Sonu", Toast.LENGTH_SHORT).show()
            progressBar.setVisibility(View.GONE)
            return
        }

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(
                GsonConverterFactory
                    .create()
            ).addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(ITunesApi::class.java)

        compositeDisposable?.add(
            retrofit.getItunesSearch(searchText, offset, limit)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleResponse)
        )


    }

    private fun handleResponse(root: Root) {
        //root içerisinde limit bilgimiz kaç ise(10,20,...vs) o kadar data vardır
        progressBar.setVisibility(View.GONE)
            listModel.addAll(root.results)

            listModel.let {
                recyclerViewAdapter = RecylerViewAdapter(it, this@MainActivity)
                recyclerView.adapter = recyclerViewAdapter
                recyclerView.getLayoutManager()!!.scrollToPosition(offset)
            }
    }
    private fun filterForWrapper(resultModel: ArrayList<ResultModel>) {
        //root içerisinde limit bilgimiz kaç ise(10,20,...vs) o kadar data vardır
        progressBar.setVisibility(View.GONE)

        resultModel.let {
            recyclerViewAdapter = RecylerViewAdapter(it, this@MainActivity)
            recyclerView.adapter = recyclerViewAdapter
            recyclerView.getLayoutManager()!!.scrollToPosition(offset)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable?.clear()
    }

    override fun onItemClick(resultModel: ResultModel) {
        Log.d(TAG,resultModel.toString())
        val intent = Intent(this,DetailsActivity::class.java)
        intent.putExtra("resultModel",resultModel)
        startActivity(intent)
    }
}
