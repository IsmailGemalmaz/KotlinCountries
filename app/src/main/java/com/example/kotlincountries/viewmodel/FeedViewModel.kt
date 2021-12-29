package com.example.kotlincountries.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kotlincountries.model.Country
import com.example.kotlincountries.service.CountryApiService
import com.example.kotlincountries.service.CountryDatabase
import com.example.kotlincountries.util.CustomSharedPeferences
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch

class FeedViewModel(application:Application): BaseViewModel(application) {

    private val countryApiService=CountryApiService()
    private val disposable=CompositeDisposable()
    private var customPreferences=CustomSharedPeferences(getApplication())
    private var refreshTime=10*60*1000*1000*1000L

    val countries= MutableLiveData<List<Country>>()
    val countryError=MutableLiveData<Boolean>()
    val countryLoading=MutableLiveData<Boolean>()

    fun refreshData(){

        val updateTime=customPreferences.getTime()
        //şuanki zman bi önce kaydettiğim zamandan 10 dk küçükse
        if(updateTime!=null&&updateTime!=0L&&System.nanoTime()-updateTime<refreshTime){
            getDataFromSQLite()
        }else{
            getDataFromAPI()
        }

    }

    fun refreshFromAPI(){
        getDataFromAPI()
    }

    private fun getDataFromSQLite(){
        countryLoading.value=true
        launch {
            val countries=CountryDatabase(getApplication()).countryDao().getAllCountries()
            showCountries(countries)
            Toast.makeText(getApplication(),"Countries From SQLite",Toast.LENGTH_LONG).show()
        }
    }

    private fun getDataFromAPI(){
        countryLoading.value=true
        disposable.add(
            countryApiService.getData()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object :DisposableSingleObserver<List<Country>>(){//single özel birşey
                    override fun onSuccess(t: List<Country>) {
                        storeInSQLite(t)//başka bir thread dde yaacağız bunun işlemlerini
                    Toast.makeText(getApplication(),"Countries From API",Toast.LENGTH_LONG).show()
                    }

                    override fun onError(e: Throwable) {
                       countryLoading.value=false
                        countryError.value=true
                        e.printStackTrace()
                    }

                })
        )
    }


    private fun showCountries(countreyList:List<Country>){
        countries.value=countreyList
        countryError.value=false
        countryLoading.value=false
    }

    private fun storeInSQLite(list:List<Country>){
        launch {
            val dao=CountryDatabase(getApplication()).countryDao()
            dao.deleteAllCountries()
          val listLong=  dao.insertAll(*list.toTypedArray())//toTypeArray listeyi tek tek yapar ist ->individual
            var i =0
            while (i<list.size){
                list[i].uuid=listLong[i].toInt()
                i=i+1
            }

            showCountries(list)
        }
        customPreferences.saveTime(System.nanoTime())
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}