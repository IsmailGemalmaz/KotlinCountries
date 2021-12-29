package com.example.kotlincountries.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlincountries.R
import com.example.kotlincountries.databinding.ItemCountryBinding
import com.example.kotlincountries.model.Country
import com.example.kotlincountries.util.downloadFromUrl
import com.example.kotlincountries.util.placeHolderProgresBar
import com.example.kotlincountries.view.FeedFragment
import com.example.kotlincountries.view.FeedFragmentDirections
import kotlinx.android.synthetic.main.item_country.view.*

class CountryAdapter(val countryList:ArrayList<Country>): RecyclerView.Adapter<CountryAdapter.CountryviewHolder>(),CountryClickListener {

    class CountryviewHolder(var view: ItemCountryBinding) :RecyclerView.ViewHolder(view.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryviewHolder {
        var inflater=LayoutInflater.from(parent.context)
        //val view=inflater.inflate(R.layout.item_country,parent,false)
        val view =DataBindingUtil.inflate<ItemCountryBinding>(inflater,R.layout.item_country,parent,false)//databindin cunstrocter bak
        return CountryviewHolder(view)
    }

    override fun onBindViewHolder(holder: CountryviewHolder, position: Int) {

        holder.view.country=countryList[position]
        holder.view.listener=this
        /*
        holder.view.name.text=countryList[position].countryName
        holder.view.region.text=countryList[position].countryRegion
        holder.view.imageView.downloadFromUrl(countryList[position].imageUrl, placeHolderProgresBar(holder.view.context))

        holder.view.setOnClickListener{
            val action=FeedFragmentDirections.actionFeedFragmentToCountryFragment(countryList[position].uuid )

            Navigation.findNavController(it).navigate(action)

        }*/
    }

    override fun getItemCount(): Int {
       return countryList.size
    }


    //ekran yukarı çekildiğinde yeni dataları buna alacağız
    fun updateCountryList(newCountryList: List<Country>){
        countryList.clear()
        countryList.addAll(newCountryList)
        notifyDataSetChanged()
    }

    override fun onCounteryClicked(v: View) {
        val uuid=v.countryUuidText.text.toString().toInt()
        val action=FeedFragmentDirections.actionFeedFragmentToCountryFragment(countryList[uuid].uuid )

        Navigation.findNavController(v).navigate(action)
    }
}