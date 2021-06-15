package ucb.codigorocha.tripplanner

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.text.Html.FROM_HTML_MODE_LEGACY
import android.text.Spanned
import android.text.method.LinkMovementMethod
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_newPlan.setOnClickListener {
            startActivity(Intent(this@MainActivity, NewPlanActivity::class.java))
        }

        btn_myPlans.setOnClickListener {
            startActivity(Intent(this@MainActivity, MyPlansActivity::class.java))
        }

        val styledText: Spanned = Html.fromHtml(getString(R.string.acesse_o_google), FROM_HTML_MODE_LEGACY)
        txtView_google.setMovementMethod(LinkMovementMethod.getInstance());
    }
}