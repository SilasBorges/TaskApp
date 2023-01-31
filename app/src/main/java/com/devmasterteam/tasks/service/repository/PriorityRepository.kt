package com.devmasterteam.tasks.service.repository

import android.content.Context
import com.devmasterteam.tasks.service.listener.APIListener
import com.devmasterteam.tasks.service.model.PriorityModel
import com.devmasterteam.tasks.service.repository.local.TaskDatabase
import com.devmasterteam.tasks.service.repository.remote.PriorityService
import com.devmasterteam.tasks.service.repository.remote.RetrofitClient

class PriorityRepository(context: Context) : BaseRepository(context) {

    private val remote = RetrofitClient.getService(PriorityService::class.java)
    private val dataBase = TaskDatabase.getDatabase(context).priorityDAO()

    companion object {
        private val cache = mutableMapOf<Int, String>()
        fun getDescription(id: Int): String {
            return cache[id] ?: ""
        }

        fun setDescription(id: Int, str: String) {
            cache[id] = str
        }
    }

    fun getDescription(id: Int): String {
        val cached = PriorityRepository.getDescription(id)
        return if (cached == "") {
            val description = dataBase.getDescription(id)
            setDescription(id, description)
            description
        } else {
            cached
        }
    }

    fun list(listener: APIListener<List<PriorityModel>>) {
        executeCall(remote.list(), listener)
    }

    fun list(): List<PriorityModel> {
        return dataBase.list()
    }

    fun save(list: List<PriorityModel>) {
        dataBase.clear()
        dataBase.save(list)
    }
}