package ucb.codigorocha.tripplanner

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_my_plans.*

class MyPlansActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_plans)

        val sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)

        val plans = sharedPreferences.all

        val entries: List<String> = plans.toList().map { "(${it.first}, ${it.second})" }
        entries.forEach { println(it) }

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, entries)
        listView_plans.adapter = adapter

        listView_plans.setOnItemClickListener { parent, view, position, id ->
            val selectedItem = parent.getItemAtPosition(position) as String
            println(selectedItem.subSequence(1, 13))

            startActivity(Intent(this@MyPlansActivity, PlanActivity::class.java).apply {
                putExtra("plan", selectedItem.subSequence(1, 13))
            })
        }
    }
}