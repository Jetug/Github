package com.example.test

import android.app.Activity
import android.view.*
import android.widget.*
import androidx.recyclerview.widget.*
import com.example.test.activity.*
import com.example.test.data.*

private const val USER_ITEM = 0
private const val REPO_ITEM = 1

private const val DIR = "dir"
private const val FILE = "file"

class ContentAdapter(val activity: Activity, val items: Array<ContentItem>, val repo: String): RecyclerView.Adapter<ContentAdapter.ViewHolder>() {
    open inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        fun bindView(item: ContentItem): View {
            return itemView.apply {
                when(item.type){
                    DIR -> {
                        findViewById<TextView>(R.id.name).text = item.name
                        findViewById<ImageView>(R.id.dir_thumbnail).setImageResource(R.drawable.ic_folder)
                        setOnClickListener{ openDir(item) }
                    }
                    FILE -> {
                        findViewById<TextView>(R.id.name).text = item.name
                        findViewById<ImageView>(R.id.dir_thumbnail).setImageResource(R.drawable.ic_file)
                        setOnClickListener{ openFile(item) }
                    }
                }
            }
        }

        private fun openDir(item: ContentItem) {
            openRepoContent(repo, item.path, activity)
        }

        private fun openFile(item: ContentItem) {
            viewFile(item.html_url, activity)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position].type) {
            DIR -> 0
            else -> 1
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(activity.layoutInflater
            .inflate(R.layout.item_content, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items.getOrNull(position) ?: return
        holder.bindView(item)
    }

    override fun getItemCount(): Int = items.size
}