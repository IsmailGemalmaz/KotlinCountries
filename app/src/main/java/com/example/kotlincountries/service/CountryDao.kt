package com.example.kotlincountries.service

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.kotlincountries.model.Country

@Dao
interface CountryDao {

    //data accsess object

    @Insert
    suspend fun insertAll(vararg countries: Country):List<Long>

    //INSERT -> Insert into
    //suspend ->coroutine, pause & resume
    //vararg -> multiple country objects
    // List<Long> -> primary key

    @Query("SELECT * FROM country")
    suspend fun  getAllCountries():List<Country>

    @Query("SELECT * FROM country WHERE uuid=:countryId ")
    suspend fun getCountry(countryId:Int):Country

    @Query("DELETE FROM country")
    suspend fun deleteAllCountries()
}