package com.enxy.noolite.features.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.enxy.noolite.R
import com.enxy.noolite.features.main.ScriptAdapter.ScriptHolder
import com.enxy.noolite.features.model.Script
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
        holder.bind(scriptList[position])
    }

    inner class ScriptHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(script: Script) = with(itemView) {
            scriptName.text = script.name
            scriptLayout.setOnClickListener { listener.onScriptExecute(script) }
            settingsButton.setOnClickListener { listener.onScriptEdit(script) }
        }
    }

    interface ScriptListener {
        fun onScriptExecute(script: Script)
        fun onScriptEdit(script: Script)
    }
}