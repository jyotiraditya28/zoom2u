package com.example.zoom2u.application.ui.details_base_page.profile.my_location.search_location

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.EditText
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.zoom2u.R
import com.example.zoom2u.application.ui.details_base_page.bid_quote_request.BidquoteRequestFragment
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse
import com.google.android.libraries.places.api.net.PlacesClient

class AddStopDialog (private val fragment: Fragment) {
    private var context: Context?=null
    private var dialogFor_AddStop: Dialog? = null
    var sessionToken: AutocompleteSessionToken? = null

    private var placesClient: PlacesClient? = null

    var arrayPfAddress_N_Fav: ArrayList<Model_AddAddress_n_Fav>? = null
    var mAdapter: Adapter_AddAddress_n_Fav? = null

    var item_AddAddressAry: Model_AddAddress_n_Fav? = null
    var progressBar: ProgressDialog? = null
    var addressLatLong_Dictionary: HashMap<String, Any>? = null
    var baseParentView: RelativeLayout?=null
    var view_dialog: View?=null

    init {
        context=fragment.activity
        if (!Places.isInitialized()) {
            val apiKey = context!!.getString(R.string.google_api_key)
            Places.initialize(context!!.applicationContext, apiKey)
        }
        sessionToken = AutocompleteSessionToken.newInstance()
        placesClient = Places.createClient(context!!)
    }

    fun addStopDialog(baseView: RelativeLayout) {
        baseParentView=baseView
        try {
            if (dialogFor_AddStop != null)
                if (dialogFor_AddStop!!.isShowing)
                    dialogFor_AddStop!!.dismiss()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        if (dialogFor_AddStop != null)
            dialogFor_AddStop = null

        view_dialog= View.inflate(context,R.layout.add_stop_dialog,null)

     //   dialogFor_AddStop = Dialog(context!!, R.style.AddStopSlideAnim)
        dialogFor_AddStop!!.setCancelable(false)
        //   dialogFor_AddStop!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        /*  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
              dialogFor_AddStop!!.window.statusBarColor = Color.WHITE
              var decorView = dialogFor_AddStop!!.window!!.decorView
              decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
          }*/

        dialogFor_AddStop!!.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogFor_AddStop!!.setContentView(view_dialog!!)

        val window = dialogFor_AddStop!!.getWindow()
        window!!.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)
        var wlp: WindowManager.LayoutParams? = window!!.getAttributes()

        wlp!!.gravity = Gravity.CENTER
        wlp.flags = wlp.flags and WindowManager.LayoutParams.FLAG_DIM_BEHIND.inv()
        window!!.setAttributes(wlp)
        wlp = null

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialogFor_AddStop!!.getWindow()!!.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            dialogFor_AddStop!!.getWindow()!!.setStatusBarColor(Color.WHITE)
            var decorView = dialogFor_AddStop!!.window!!.decorView
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        var recycler_AddAddressView = dialogFor_AddStop!!.findViewById<RecyclerView>(R.id.recycler_AddAddressView)
        var edtWhereTo_AddAddressView = dialogFor_AddStop!!.findViewById<EditText>(R.id.edtWhereTo_AddAddressView)
        var backBtn_AddAddressView = dialogFor_AddStop!!.findViewById<ImageView>(R.id.backBtn_AddAddressView)
        var imageview_cross = dialogFor_AddStop!!.findViewById<ImageView>(R.id.imageview_cross)
        var layoutParent = dialogFor_AddStop!!.findViewById<RelativeLayout>(R.id.layoutParent)
        layoutParent.visibility= View.INVISIBLE

        Handler().postDelayed(object:Runnable{
            override fun run() {
                layoutParent.visibility= View.VISIBLE
            }
        },400)




        dialogFor_AddStop!!.setOnShowListener {
            Log.d("TAG","Dialog is showing")
            /*  Log.d("TAG","dialog show")
              if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                  revealShow(view_dialog!!,true,null)
              }*/
        }


//        val colorTran=context!!.resources.getColor(R.color.color_black_transparent_bg)
//        val colorWhite=context!!.resources.getColor(R.color.color_white)
//        val backgroundAnimator=ValueAnimator.ofObject(ArgbEvaluator(),0,colorWhite)
//
//        backgroundAnimator.addUpdateListener {
//            layoutParent.setBackgroundColor(it.getAnimatedValue() as Int)
//        }
//        backgroundAnimator.duration=500
//        backgroundAnimator.start()


        //animation for set backButton alpha
        var imageResourceAnimator = ValueAnimator.ofFloat(0.3f, 1f)
        imageResourceAnimator.setDuration(1200)

        imageResourceAnimator.addUpdateListener {
            backBtn_AddAddressView.alpha = it.animatedValue as Float
            edtWhereTo_AddAddressView.alpha=it.animatedValue as Float
        }

        imageResourceAnimator.start()

        imageview_cross.setOnClickListener {
            edtWhereTo_AddAddressView.setText("")
        }

        backBtn_AddAddressView.setOnClickListener {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                revealShow(view_dialog!!,false,dialogFor_AddStop)
//            }
            dialogFor_AddStop!!.dismiss()
        }

        dialogFor_AddStop!!.setOnKeyListener(object : DialogInterface.OnKeyListener{
            override fun onKey(dialog: DialogInterface?, keyCode: Int, event: KeyEvent?): Boolean {
                if (keyCode == KeyEvent.KEYCODE_BACK){
                    dialogFor_AddStop!!.dismiss()
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                        revealShow(view_dialog!!,false,dialogFor_AddStop)
//                    }
                    return true
                }
                return false
            }

        })


        edtWhereTo_AddAddressView.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s!!.count() > 0) {
                    recycler_AddAddressView!!.visibility = View.VISIBLE
                    arrayPfAddress_N_Fav?.clear()
                    placeAPICallForAddress(edtWhereTo_AddAddressView.text.toString())
                } else
                    recycler_AddAddressView!!.visibility = View.GONE
            }
        })


        var arrayPfAddress_N_Fav = ArrayList<Model_AddAddress_n_Fav>()

//        arrayPfAddress_N_Fav.add(Model_AddAddress_n_Fav(1, "Hewitt Street", "Greenacre NSW"))
//        arrayPfAddress_N_Fav.add(Model_AddAddress_n_Fav(1, "Hewitt Avenue", "Wahroonga NSW"))
//        arrayPfAddress_N_Fav.add(Model_AddAddress_n_Fav(1, "Aon Hewitt Financial Advice", "Kent street, Sydney NSW"))
//        arrayPfAddress_N_Fav.add(Model_AddAddress_n_Fav(1, "Hewitt Taylor", "Hunter street, Sydney NSW"))
//        arrayPfAddress_N_Fav.add(Model_AddAddress_n_Fav(1, "Hewitt Avenue", "Greystanes NSW"))
//        arrayPfAddress_N_Fav.add(Model_AddAddress_n_Fav(1, "Hewitt Street", "Colyton NSW"))

        //  arrayPfAddress_N_Fav = ArrayList<Model_AddAddress_n_Fav>()

        mAdapter = Adapter_AddAddress_n_Fav(context!!, arrayPfAddress_N_Fav!!, object : Adapter_AddAddress_n_Fav.OnItemClickListener {
            override fun onItemClick(item: Model_AddAddress_n_Fav) {
                /*item_AddAddressAry = item
                edtWhereTo_AddAddressView!!.setText("")
                recycler_AddAddressView!!.visibility = View.GONE
                GetAddressDetails_GeoCoder().execute()*/

                //val addAddressIntent = Intent(context, AddStopDetail_Activity::class.java)
               // addAddressIntent.putExtra("item_add_stop", item)
                //  addAddressIntent.putExtra("address_lat_long_dic",addressLatLong_Dictionary)
                //context!!.startActivity(addAddressIntent)
            }
        })
        edtWhereTo_AddAddressView.setOnClickListener {
            recycler_AddAddressView.adapter = mAdapter
        }



        dialogFor_AddStop!!.show()
    }

    private fun placeAPICallForAddress(searchString: String) {
        try {
            var requestBuilder =
                FindAutocompletePredictionsRequest.builder()
                    .setQuery(searchString)
//                            .setCountry("AU")
                    .setTypeFilter(TypeFilter.ADDRESS)

            requestBuilder.setSessionToken(sessionToken)

            var task = placesClient?.findAutocompletePredictions(requestBuilder.build())

            task?.addOnSuccessListener(OnSuccessListener {
                stringify(it, false)
            })

            task?.addOnFailureListener(OnFailureListener {
                it.printStackTrace()
            })

            task?.addOnCompleteListener(OnCompleteListener {

            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    fun stringify(response: FindAutocompletePredictionsResponse, raw: Boolean) {
        for (autocompletePrediction: AutocompletePrediction in response.getAutocompletePredictions()) {
            //        loadData(Model_AddAddress_n_Fav(0, autocompletePrediction.getPrimaryText(null).toString(), autocompletePrediction.getFullText(/* matchStyle */ null).toString()))
        }
        mAdapter?.notifyDataSetChanged()
    }

    private fun loadData(modal_StopShareData: Model_AddAddress_n_Fav) {
        arrayPfAddress_N_Fav!!.add(modal_StopShareData)
    }






}