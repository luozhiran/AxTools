package com.ax.axtools

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.ax.axtools.model.SpaceModel
import com.ax.axtools_library.utils.AxDataTool
import com.ax.axtools_library.utils.AxFileTool

import java.io.File
import java.util.ArrayList
import java.util.Locale

import butterknife.BindView
import butterknife.ButterKnife

class FreeSpaceActivity : AppCompatActivity() {

    @BindView(R.id.recycler)
    internal var recycler: RecyclerView? = null
    @BindView(R.id.sd0)
    internal var sd0: TextView? = null
    @BindView(R.id.sd1)
    internal var sd1: TextView? = null
    private var myAdapter: MyAdapter? = null

    private var sd0Str: String? = null
    private var sd1Str = "--"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_free_space)
        ButterKnife.bind(this)
        val spaceModel = SpaceModel()
        spaceModel.path = AxFileTool.getSDCardPath() + "childyouku/"
        val use = AxDataTool.byte2FitSize(AxFileTool.getFileAllSize(spaceModel.path))
        val stringBuilder = StringBuilder()
        stringBuilder.append("已用：").append(use)
        spaceModel.size = stringBuilder.toString()
        spaceModel.name = "小小优酷"
        myAdapter = MyAdapter(this)
        myAdapter!!.setData(spaceModel)
        recycler!!.layoutManager = LinearLayoutManager(this)
        recycler!!.adapter = myAdapter
        val list = AxFileTool.getExternalStorageVolume(applicationContext)
        val rate0 = (1 - AxFileTool.getSDCardAvailaleSize().toDouble() / AxFileTool.getSDTotalSize()) * 100
        sd0Str = String.format(Locale.getDefault(), "%.2f", rate0)
        if (list.size > 0) {
            val file = File(list[0])
            if (file.exists() && file.length() > 0) {
                val rate1 = (1 - AxFileTool.getDirSize(list[0]).toDouble() / AxFileTool.getExternalSDTotalSize(list[0])) * 100
                sd1Str = String.format(Locale.getDefault(), "%.2f", 1 - rate1)
                sd1Str = "已用：$sd0Str%"
            }
        }
        sd0!!.text = "已用：$sd0Str%"
        sd1!!.text = sd1Str
    }

    private class MyAdapter(private val context: Context) : RecyclerView.Adapter<MyHolder>() {

        private val list: MutableList<SpaceModel>

        init {
            list = ArrayList()
        }

        fun setList(list: List<SpaceModel>) {
            this.list.addAll(list)
        }

        fun setData(spaceModel: SpaceModel) {
            this.list.add(spaceModel)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
            return MyHolder(LayoutInflater.from(context).inflate(R.layout.free_space_item, parent, false))
        }

        override fun onBindViewHolder(holder: MyHolder, position: Int) {
            holder.name!!.text = list[position].name
            holder.size!!.text = list[position].size
        }

        override fun getItemCount(): Int {
            return list.size
        }
    }


    class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @BindView(R.id.name)
        internal var name: TextView? = null
        @BindView(R.id.size)
        internal var size: TextView? = null
        @BindView(R.id.option)
        internal var option: TextView? = null

        init {
            ButterKnife.bind(this, itemView)
        }
    }
}
