package wq.gdky005.ip.dmeo

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject
import wq.gdky005.ip.demo.databinding.ActivitySpDetailMainBinding

class SPDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySpDetailMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySpDetailMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val time = intent.getStringExtra(MainActivity.SP_KEY_TIMESTAMP)
        val info = intent.getStringExtra(MainActivity.SP_KEY_INFO)

        val obj = JSONObject(info)
        val text = obj.optString(MainActivity.SP_KEY_BASE_TEXT)
        val responseText =  obj.optString(MainActivity.SP_KEY_RESPONSE_TEXT)

        binding.tvTime.text = time
        binding.tvBaseInfo.text = text
        binding.tvResponseInfo.text = responseText

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
