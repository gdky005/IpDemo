package wq.gdky005.ip.dmeo

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import com.blankj.utilcode.util.NetworkUtils
import com.blankj.utilcode.util.ShellUtils
import com.blankj.utilcode.util.ToastUtils
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {

    var IP_URL = "cip.cc"

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        getIp()

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {//function
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {//function

                val url = parent!!.adapter.getItem(position).toString()
                IP_URL = url
                ToastUtils.showShort("你选择的检测域名是：$url")
            }

        }

        fab.setOnClickListener { view ->
            getIp()

            Snackbar.make(view, getIp(), Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }

    private fun getIp(): String {

        val ip = NetworkUtils.getIPAddress(true)
        val isWifi = NetworkUtils.isWifiAvailable()
        val wifiIp = NetworkUtils.getIpAddressByWifi()
        val cmdIpInfo = ShellUtils.execCmd("curl $IP_URL", false)

        val text = """
                获取当前的网络 ip 是：$ip.

                Wifi 状态：$isWifi, wifiIp: $wifiIp.

                curl 获取的 ip 信息：

                $cmdIpInfo


            """


        tv.text = ""
        tv.text = text
        return text
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
