package wq.gdky005.ip.dmeo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_sp_detail_main.*
import org.json.JSONObject

class SPDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sp_detail_main)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val time = intent.getStringExtra(MainActivity.SP_KEY_TIMESTAMP)
        val info = intent.getStringExtra(MainActivity.SP_KEY_INFO)

        val obj = JSONObject(info)
        val text = obj.optString(MainActivity.SP_KEY_BASE_TEXT)
        val responseText =  obj.optString(MainActivity.SP_KEY_RESPONSE_TEXT)

        tv_time.text = time
        tv_base_info.text = text
        tv_response_info.text = responseText

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}
