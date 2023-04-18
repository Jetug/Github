package com.example.test

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.test.activity.*
import com.example.test.data.GithubItem
import com.example.test.data.Repo
import com.example.test.data.User


private const val USER_ITEM = 0
private const val REPO_ITEM = 1

class GitHubAdapter(val activity: Activity, val items: ArrayList<GithubItem>): RecyclerView.Adapter<GitHubAdapter.ViewHolder>() {
    open inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        fun bindView(item: GithubItem): View {
            return itemView.apply {
                when(item){
                    is User -> {
                        findViewById<TextView>(R.id.name).text = item.login
                        findViewById<TextView>(R.id.score).text = item.score.toString()
                        findViewById<ImageView>(R.id.dir_thumbnail).load(item.avatar_url)
                        setOnClickListener{clickUser(item)}

                    }
                    is Repo -> {
                        findViewById<TextView>(R.id.name).text = item.name
                        findViewById<TextView>(R.id.forks).text = item.forks.toString()
                        findViewById<TextView>(R.id.description).text = item.description
                        setOnClickListener{clickRepo(item)}
                    }
                }

            }
        }

        private fun clickUser(item: User) {
            activity.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(item.html_url)))
        }

        private fun clickRepo(item: Repo) {
            openRepoContent(item.full_name, "", activity)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is User -> USER_ITEM
            else -> REPO_ITEM
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutType = when {
            viewType == USER_ITEM -> R.layout.item_user
            else -> R.layout.item_repo
        }

        val view = activity.layoutInflater.inflate(layoutType, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items.getOrNull(position) ?: return
        holder.bindView(item)
    }

    override fun getItemCount(): Int = items.size
}