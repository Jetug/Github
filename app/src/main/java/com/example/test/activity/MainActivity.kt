package com.example.test.activity

import android.R
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.test.*
import com.example.test.data.GithubItem
import com.example.test.data.Repo
import com.example.test.data.User
import com.example.test.data.sendGet
import com.example.test.databinding.ActivityMainBinding
import com.google.gson.Gson
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import org.json.JSONObject


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.search.setOnClickListener(::searchButtonClick)
        binding.searchField.addTextChangedListener(
            object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    binding.search.isEnabled = count >= 3
                }
                override fun afterTextChanged(s: Editable) {}
            }
        )
    }
    private fun searchButtonClick(view: View){
        binding.search.isEnabled = false
        binding.searchField.isEnabled = false
        binding.progressBar.visibility = View.VISIBLE
        binding.recyclerView.adapter = null

        startSearch{
            binding.progressBar.visibility = View.GONE
            binding.recyclerView.adapter = GitHubAdapter(this, it)
            binding.recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
            binding.search.isEnabled      = true
            binding.searchField.isEnabled = true
        }
    }

    private fun startSearch(callback: (ArrayList<GithubItem>) -> Unit) = CoroutineScope(IO).launch{
        val items = ArrayList<GithubItem>()
        val query = binding.searchField.text

        val usersDef = async{ sendGet("https://api.github.com/search/users?q=${query}") }
        val reposDef = async{ sendGet(" https://api.github.com/search/repositories?q=${query}") }
        val usersJson = usersDef.await()
        val reposJson = reposDef.await()

        val jsonArrUsers = JSONObject(usersJson).getJSONArray("items").toString()
        val jsonArrRepos = JSONObject(reposJson).getJSONArray("items").toString()

        val users = Gson().fromJson(jsonArrUsers, Array<User>::class.java)
        val repos = Gson().fromJson(jsonArrRepos, Array<Repo>::class.java)

        items.addAll(users)
        items.addAll(repos)

        items.sortBy {
            when (it) {
                is User -> it.login
                is Repo -> it.name
                else -> ""
            }
        }

        withContext(Main) { callback(items) }

        println(items)
        println(users)
        println(repos)
    }

    suspend fun sum(a: Int, b: Int) : Int{
        delay(500L) // имитация продолжительной работы
        return a + b
    }




//    private fun keyInput(view: View): Boolean{
//
//    }
}
