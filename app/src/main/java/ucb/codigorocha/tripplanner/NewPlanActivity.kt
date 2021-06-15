package ucb.codigorocha.tripplanner

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_new_plan.*
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashSet


class NewPlanActivity : AppCompatActivity() {
    lateinit var placesClient: PlacesClient

    var placeFields = Arrays.asList(
        Place.Field.ID,
        Place.Field.NAME,
        Place.Field.ADDRESS
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_plan)
        var fromCoin: String?
        var toCoin: String?

        dateWatcher(input_dateGo)
        dateWatcher(input_dateBack)

        initPlaces()
        setupPlacesAutocomplete()

        ArrayAdapter.createFromResource(
            this,
            R.array.moedas,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            drop_toCoin.adapter = adapter
            drop_fromCoin.adapter = adapter
        }


        btn_myPlans.setOnClickListener {
            fromCoin = drop_fromCoin.selectedItem.toString()
            toCoin = drop_toCoin.selectedItem.toString()

            val queue = Volley.newRequestQueue(this)
            val url = "https://economia.awesomeapi.com.br/last/$fromCoin-$toCoin"

            val jsonObjectRequest = JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                { response ->
                    val list = hashSetOf<String>()
                    val jsonObject: JSONObject = response.getJSONObject(fromCoin + toCoin)
                    val convertedValue = jsonObject.getString("bid")

                    list.addAll(
                        hashSetOf(
                            view_place.text.toString(),
                            input_dateGo.text.toString(),
                            input_dateBack.text.toString(),
                            input_totalValue.text.toString(),
                            convertedValue.toString()
                        )
                    )

                    saveDataFromArray("plan" + input_dateGo.text.toString(),list)

//                    saveData(
//                        view_place.text.toString(),
//                        input_dateGo.text.toString(),
//                        input_dateBack.text.toString(),
//                        input_totalValue.text.toString(),
//                        convertedValue
//                    )
                    startActivity(Intent(this@NewPlanActivity, MainActivity::class.java))
                },
                { error ->
                    println(error.toString())
                }
            )
            queue.add(jsonObjectRequest)
        }


//        val queue = Volley.newRequestQueue(this)
//        val url = "https://pt.flightaware.com/live/flight/JST514"
//
//        val jsonObjectRequest = JsonObjectRequest(
//            Request.Method.GET,
//            url,
//            null,
//            { response ->
//                println(response.toString())
//            },
//            { error ->
//
//            }
//        )
//
//        queue.add(jsonObjectRequest)

//        val stringRequest = StringRequest(
//            Request.Method.GET,
//            url,
//            Response.Listener<String> { response ->
//                val gson = GsonBuilder().create()
//
//                val result = response.get
//                )
//            }

    }

    private fun saveDataFromArray(key: String, plans: HashSet<String>) {
        val sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        plans.forEach { plan -> println(plan) }
        editor.putStringSet(key, plans)
//        editor.putString("plans", Gson().toJson(plans))
        editor.apply()
        Toast.makeText(this, "PLANEJAMENTO SALVO", Toast.LENGTH_LONG).show()
    }

    private fun saveData(
        address: String,
        dateGo: String,
        dateBack: String,
        totalValue: String,
        convertedValue: String
    ) {
        val sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.apply {
            putString("Address", address)
            putString("dateGo", dateGo)
            putString("dateBack", dateBack)
            putFloat("totalValue", totalValue.toFloat())
            putFloat("convertedValue", convertedValue.toFloat())
        }.apply()
        Toast.makeText(this, "PLANEJAMENTO SALVO", Toast.LENGTH_LONG).show()
    }

    private fun setupPlacesAutocomplete() {
        val autocompleteFragment = supportFragmentManager
            .findFragmentById(R.id.fragment_place) as AutocompleteSupportFragment
        autocompleteFragment.setPlaceFields(placeFields)

        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(p0: Place) {
                view_place.text = p0.address
            }

            override fun onError(p0: Status) {
                Toast.makeText(this@NewPlanActivity, p0.statusMessage, Toast.LENGTH_LONG).show()
            }

        })

    }

    private fun initPlaces() {
        Places.initialize(this, getString(R.string.maps_api_key))
        placesClient = Places.createClient(this)
    }

    fun dateWatcher(editText: EditText) {

        var oldString: String = ""

        editText.addTextChangedListener(object : TextWatcher {
            var changed: Boolean = false

            override fun afterTextChanged(p0: Editable?) {

                changed = false


            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                editText.setSelection(p0.toString().length)
            }

            @SuppressLint("SetTextI18n")
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                var str: String = p0.toString()

                if (str != null) {

                    if (oldString.equals(str)) {

                    } else if (str.length == 2) {
                        var element0: String = str.elementAt(0).toString()
                        var element1: String = str.elementAt(1).toString()
                        str = element0 + element1 + "/"
                        editText.setText(str)
                        oldString = element0 + element1
                        editText.setSelection(str.length)

                    } else if (str.length == 5) {

                        var element0: String = str.elementAt(0).toString()
                        var element1: String = str.elementAt(1).toString()
                        var element2: String = str.elementAt(2).toString()
                        var element3: String = str.elementAt(3).toString()
                        var element4: String = str.elementAt(4).toString()

                        str = element0 + element1 + element2 + element3 + element4 + "/"
                        editText.setText(str)
                        oldString = element0 + element1 + element2 + element3 + element4
                        editText.setSelection(str.length)

                    } else if (str.length > 8) {

                        str = str.substring(0, str.length - 1)
                        editText.setText(str)
                        editText.setSelection(str.length)

                    }


                }

            }
        })
    }

}