package wq.gdky005.ip.dmeo

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.blankj.utilcode.util.NetworkUtils
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.ShellUtils
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import org.json.JSONObject




class MainActivity : AppCompatActivity() {

    companion object {
        var SP_FILE_NAME = "sp_file_name"
        var SP_KEY_TIMESTAMP = "sp_key_timestamp"
        var SP_KEY_BASE_TEXT = "sp_key_base_text"
        var SP_KEY_RESPONSE_TEXT = "sp_key_response_text"
        var SP_KEY_INFO = "sp_key_info"


        var FLAG_MSG_UPDATE_UI = 1
        var IP_URL = "cip.cc"
    }

    private val handler : Handler = Handler{
        when(it.what){
            FLAG_MSG_UPDATE_UI ->{
                val list = it.obj as List<String>
                val text = list[0]
                val responseText = list[1]

                tv_base_info.text = text
                tv_response_info.text = responseText

                val obj = JSONObject()
                obj.putOpt(SP_KEY_TIMESTAMP, System.currentTimeMillis())
                obj.putOpt(SP_KEY_BASE_TEXT, text)
                obj.putOpt(SP_KEY_RESPONSE_TEXT, responseText)



                SPUtils.getInstance(SP_FILE_NAME).put(""+System.currentTimeMillis(), obj.toString())

                swipeRefreshLayout.isRefreshing = false
                Snackbar.make(fab, IP_URL + resources.getString(R.string.check_finish), Snackbar.LENGTH_LONG)
                    .setAction("", null).show()
            }
            else -> {
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener{
            finish()
        }

        swipeRefreshLayout.setProgressViewEndTarget(true, 150)
        swipeRefreshLayout.setSize(SwipeRefreshLayout.LARGE)
        swipeRefreshLayout.setProgressBackgroundColor(R.color.colorPrimary)
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent)
        swipeRefreshLayout.setOnRefreshListener{
            getIp()
        }


        val ips  = resources.getStringArray(R.array.ips)
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, ips)
        spinner.adapter = spinnerAdapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {//function
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {//function
                val url = parent!!.adapter.getItem(position).toString()
                IP_URL = url
                getIp()
            }
        }
        spinner.setSelection(ips.indexOf(IP_URL))

        fab.setOnClickListener {
            getIp()
        }

        clearAllData()
    }

    /**
     * 当达到 X 条记录的时候，清空所有的
     */
    private fun clearAllData() {
        val isClean = SPUtils.getInstance(SP_FILE_NAME).all.size > 200
        if (isClean)
            SPUtils.getInstance(SP_FILE_NAME).clear()
    }

    private fun getIp() {
        swipeRefreshLayout.isRefreshing = true
        Thread {
            val ip = NetworkUtils.getIPAddress(true)
            val isWifi = NetworkUtils.isWifiAvailable()
            val wifiIp = NetworkUtils.getIpAddressByWifi()
            val cmdIpInfo = ShellUtils.execCmd("curl $IP_URL", false)

            val text = getString(R.string.current_network_info) + ip +  getString(R.string.wifi_state_text) + isWifi + " ===> " + wifiIp

            var responseText = getString(R.string.current_network_curl_title)

            responseText += if (cmdIpInfo.result == 0) {
                cmdIpInfo.successMsg
            } else {
                getString(R.string.current_network_error_info) + cmdIpInfo.errorMsg
            }

            val msg : Message = handler.obtainMessage()
            msg.what = FLAG_MSG_UPDATE_UI

            val list = mutableListOf<String>()
            list.add(text)
            list.add(responseText)

            msg.obj = list
            handler.sendMessage(msg)
        }.start()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_ip_history -> {
                startActivity(Intent(this, SPListActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /**
     * 显示menu的icon,通过反射,设置Menu的icon显示.
     * @param view
     * @param menu
     * @return
     */
    @SuppressLint("RestrictedApi")
    override fun onPrepareOptionsPanel(view: View?, menu: Menu?): Boolean {
        if (menu != null) {
            if (menu::class.java.simpleName == "MenuBuilder") {
                try{

                    val method = menu.javaClass.getDeclaredMethod("setOptionalIconsVisible", java.lang.Boolean.TYPE)
                    method.isAccessible = true
                    method.invoke(menu, true)
                } catch (e: Exception) {
                    Log.e("TAG", "onMenuOpened...unable to set icons for overflow menu", e)
                }
            }
        }
        return super.onPrepareOptionsPanel(view, menu)
    }
}
