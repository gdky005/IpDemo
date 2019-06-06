package wq.gdky005.ip.dmeo

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.TimeUtils
import kotlinx.android.synthetic.main.activity_sp_list_main.*
import java.util.*


class SPListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sp_list_main)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

//        swipeRefreshLayout.setProgressViewEndTarget(true, 150)
//        swipeRefreshLayout.setSize(SwipeRefreshLayout.LARGE)
//        swipeRefreshLayout.setProgressBackgroundColor(R.color.colorPrimary)
//        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent)
//        swipeRefreshLayout.setOnRefreshListener{
////            getIp()
//        }

        val allSPData = SPUtils.getInstance(MainActivity.SP_FILE_NAME).all

        //这里将map.entrySet()转换成list
        val list = allSPData.keys.toMutableList()
        //然后通过比较器来实现排序
        Collections.sort(list, object : Comparator<String> {
            //降序排序
            override fun compare(o1: String?, o2: String?): Int {
                return o2!!.toLong().compareTo(o1!!.toLong())
            }
        })
        val list2 = mutableListOf<ListItem>()
        list.forEach {
            list2.add(ListItem(TimeUtils.millis2String(it.toLong())))
        }
        val adapter = SPListAdapter(R.layout.item_list, list2)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        adapter.setOnItemClickListener { _, _, position ->
            startActivity(
                Intent(this, SPDetailActivity::class.java)
                    .putExtra(MainActivity.SP_KEY_INFO, allSPData[list[position]] as String)
                    .putExtra(MainActivity.SP_KEY_TIMESTAMP, TimeUtils.millis2String(list[position].toLong()))
            )
        }
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
