package wq.gdky005.ip.dmeo

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import wq.gdky005.ip.demo.R

class SPListAdapter(layoutResId: Int, data: MutableList<ListItem>?) :
    BaseQuickAdapter<ListItem, BaseViewHolder>(layoutResId, data) {

    override fun convert(helper: BaseViewHolder, item: ListItem) {
        helper.setText(R.id.tv, item.time)
    }

}