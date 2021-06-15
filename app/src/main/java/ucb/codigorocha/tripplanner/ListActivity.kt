package ucb.codigorocha.tripplanner

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.AdapterView
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_list.*

public abstract class ListActivity : AppCompatActivity(), AdapterView.OnItemClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var viagens = arrayOf("")
        var adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, viagens)
        list_view_viagem.adapter= adapter

        list_view_viagem.onItemClickListener = AdapterView.OnItemClickListener{
            parent,view,position, id ->

            var item = parent.getItemAtPosition(position)
          // Imagem// var intent= Intent(this,ActivityViagem::class.java)
            intent.putExtra("img", item.toString())
        }
}
}