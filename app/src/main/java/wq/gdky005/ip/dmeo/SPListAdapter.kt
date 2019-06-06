package wq.gdky005.ip.dmeo

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class SPListAdapter(layoutResId: Int, data: MutableList<ListItem>?) :
    BaseQuickAdapter<ListItem, BaseViewHolder>(layoutResId, data) {

    override fun convert(helper: BaseViewHolder, item: ListItem) {
        helper.setText(R.id.tv, item.time)
    }

}