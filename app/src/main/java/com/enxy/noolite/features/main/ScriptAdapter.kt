package com.enxy.noolite.features.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.enxy.noolite.R
import com.enxy.noolite.core.data.Script
import com.enxy.noolite.features.main.ScriptAdapter.ScriptHolder
import kotlinx.android.synthetic.main.item_script.view.*

class ScriptAdapter(val listener: ScriptListener) : RecyclerView.Adapter<ScriptHolder>() {
    // Test Data
    private val scriptList: ArrayList<Script> = ArrayList()

    fun updateData(data: ArrayList<Script>) {
        this.scriptList.clear()
        this.scriptList.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScriptHolder {
        val view =
            LayoutInflater.from(parent.context!!).inflate(R.layout.item_script, parent, false)
        return ScriptHolder(view)
    }

    override fun getItemCount(): Int = scriptList.size

    override fun onBindViewHolder(holder: ScriptHolder, position: Int) {
        holder.bind(scriptList[position], position)
    }

    inner class ScriptHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(script: Script, position: Int) = with(itemView) {
            scriptName.text = script.name
            scriptLayout.setOnClickListener { listener.onScriptExecute(script) }
            removeScript.setOnClickListener {
                listener.onScriptRemove(script)
                scriptList.remove(script)
                if (scriptList.isEmpty())
                    listener.onEmptyScriptList()
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, scriptList.size)
            }
        }
    }

    interface ScriptListener {
        fun onEmptyScriptList()
        fun onScriptExecute(script: Script)
        fun onScriptRemove(script: Script)
    }
}