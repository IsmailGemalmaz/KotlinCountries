package com.example.kotlincountries.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kotlincountries.R
import com.example.kotlincountries.adapter.CountryAdapter
import com.example.kotlincountries.viewmodel.FeedViewModel
import kotlinx.android.synthetic.main.fragment_feed.*

class FeedFragment : Fragment() {

    private lateinit var  viewModel:FeedViewModel
    private val countryAdapter=CountryAdapter(arrayListOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_feed, container, false)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //ViewModelProviders.of(this).get(FeedViewModel::class.java) kalktı kullanımdan
        //viewmodel ile view birbirine bağladık
        viewModel=ViewModelProvider(this).get(FeedViewModel::class.java)
        viewModel.refreshData()

        //rcyclerview intialize
        countryList.layoutManager=LinearLayoutManager(context)
        countryList.adapter=countryAdapter

        //REFRESH
        swipeRefreshLayout.setOnRefreshListener {
                countryList.visibility=View.GONE
                countryError.visibility=View.GONE
                countryLoading.visibility=View.VISIBLE
                swipeRefreshLayout.isRefreshing=false
                viewModel.refreshFromAPI()
        }

        observeLiveData()

        /*
        fragmentBtn.setOnClickListener{
            val action=FeedFragmentDirections.actionFeedFragmentToCountryFragment()
            Navigation.findNavController(it).navigate(action)
        }*/
    }


    //dataları gözlemlemek için bu metodu yazdık
   private fun observeLiveData(){
        viewModel.countries.observe(viewLifecycleOwner, Observer { countries ->
            countries?.let {
                countryList.visibility = View.VISIBLE
                countryAdapter.updateCountryList(countries)
                countryError.visibility=View.GONE
            }
        })

        viewModel.countryError.observe(viewLifecycleOwner, Observer { error->
            error?.let {
                if(it){
                    countryError.visibility=View.VISIBLE
                }else{
                    countryError.visibility=View.GONE
                }
            }
        })

        viewModel.countryError.observe(viewLifecycleOwner, Observer { loading->
            loading?.let {
                if(it){
                    countryLoading.visibility=View.VISIBLE
                    countryList.visibility=View.GONE
                    countryError.visibility=View.GONE
                }else{
                    countryLoading.visibility=View.GONE
                }
            }
        })
    }

}