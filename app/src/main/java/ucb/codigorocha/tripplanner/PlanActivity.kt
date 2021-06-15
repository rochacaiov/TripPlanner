package ucb.codigorocha.tripplanner

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_plan.*

class PlanActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plan)
        val sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)

        println("STRING EXTRA NEW ACITIVIT; " + intent.getStringExtra("plan"))

        val plan = sharedPreferences.all[intent.getStringExtra("plan")]

        sharedPreferences.getStringSet(intent.getStringExtra("plan"), mutableSetOf())
            ?.forEach { parameter ->
                if (parameter.contains("/")) {
                    txtResponse_dataPartida.text = parameter
                }
                if (parameter.contains("/")) {
                    txtResponse_dataVolta.text = parameter
                }
                if (parameter.contains(".")) {
                    txtResponse_conversao.text = parameter
                }
                if (parameter.contains(",")) {
                    txtResponse_localDestino.text = parameter
                }
                if (!parameter.contains(".") && !parameter.contains("/") && !parameter.contains(",")) {
                    txtResponse_orcamento.text = parameter
                }
            }
//        Set<String> fetch = editor.getStringSet("List", null);

        println(plan)

//        txtResponse_localDestino.setText(plan)

    }
}