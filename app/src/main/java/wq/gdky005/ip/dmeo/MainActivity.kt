package wq.gdky005.ip.dmeo

import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.design.widget.Snackbar
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.blankj.utilcode.util.NetworkUtils
import com.blankj.utilcode.util.ShellUtils
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {

    companion object {
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

                swipeRefreshLayout.isRefreshing = false
                Snackbar.make(fab, "$IP_URL 检测完成", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
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
        getIp()

        fab.setOnClickListener {
            getIp()
        }
    }

    private fun getIp() {
        swipeRefreshLayout.isRefreshing = true
        Thread {
            val ip = NetworkUtils.getIPAddress(true)
            val isWifi = NetworkUtils.isWifiAvailable()
            val wifiIp = NetworkUtils.getIpAddressByWifi()
            val cmdIpInfo = ShellUtils.execCmd("curl $IP_URL", false)

            val text = "当前的网络 ip 是：$ip \n Wifi 状态：$isWifi ===> $wifiIp"

            var responseText = "curl 获取的 ip 信息：\n\n\n"

            responseText += if (cmdIpInfo.result == 0) {
                cmdIpInfo.successMsg
            } else {
                "错误信息：\n\n${cmdIpInfo.errorMsg}"
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
}
