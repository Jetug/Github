package com.example.test.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.test.*
import com.example.test.data.ContentItem
import com.example.test.data.sendGet
import com.example.test.databinding.ActivityRepositoryContentBinding
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

private const val REPO_NAME = "repoName"
private const val PATH = "path"

fun openRepoContent(repoName:String, path: String, context: Context) {
    val intent = Intent(context, RepositoryContentActivity::class.java).apply {
        putExtra(REPO_NAME, repoName)
        putExtra(PATH, path)
    }
    context.startActivity(intent)
}

class RepositoryContentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRepositoryContentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRepositoryContentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val repo = intent.getStringExtra(REPO_NAME)
        val path = intent.getStringExtra(PATH) ?: ""

        loadContent(repo!!, path){
            binding.recyclerView.adapter = ContentAdapter(this, it, repo)
            binding.recyclerView.layoutManager = LinearLayoutManager(this@RepositoryContentActivity)
        }

    }

    private fun loadContent(repo: String, path: String, callback: (Array<ContentItem>) -> Unit) = CoroutineScope(Dispatchers.IO).launch {
        val res = sendGet("https://api.github.com/repos/${repo}/contents/$path")

        val content: Array<ContentItem>

//        if( JSONObject(res).has("entries")){
//            val jsonContent = JSONObject(res).getJSONArray("entries").toString()
//            content = Gson().fromJson(jsonContent, Array<ContentItem>::class.java)
//        }
//        else {
//            content = Gson().fromJson(res, Array<ContentItem>::class.java)
//        }

        content = Gson().fromJson(res, Array<ContentItem>::class.java)


        withContext(Dispatchers.Main) { callback(content) }
    }
}