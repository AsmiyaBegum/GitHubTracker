package com.ab.githubtrackerapplication.gitRepo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ab.githubtrackerapplication.model.GitRepositoryDetail
import com.ab.githubtrackerapplication.service.GitRepoService
import com.ab.githubtrackerapplication.util.Utils
import com.ab.githubtrackerapplication.util.Utils.evictResult
import kotlinx.coroutines.*

class GitRepoViewModel : ViewModel() {

    private val gitRepositoryService = GitRepoService()
    private val networkScope = CoroutineScope(Dispatchers.IO)


    private val _repositoryDetail = MutableLiveData<GitRepositoryDetail>()
    val repositoryDetail: LiveData<GitRepositoryDetail>
        get() = _repositoryDetail

    private val _errorDetail = MutableLiveData<Unit>()
    val errorDetail: LiveData<Unit>
        get() = _errorDetail

    private val _repositoryDetailList = MutableLiveData<List<GitRepositoryDetail>>()
    val repositoryDetailList: LiveData<List<GitRepositoryDetail>>
        get() = _repositoryDetailList

    fun fetchRepositoryDetail(owner: String,repoName : String) {
        networkScope.launch {
            try {
                val resultUser = gitRepositoryService.getRepositoryDetail(owner,repoName)
                withContext(Dispatchers.Main) {
                    resultUser.onSuccess { repoDetail ->
                        _repositoryDetail.value = repoDetail
                        Utils.realmDefaultInstance { realm ->
                            realm.executeTransaction {
                                realm.copyToRealmOrUpdate(repoDetail)
                            }
                        }
                    }
                    resultUser.onFailure {
                        _errorDetail.value = Unit
                    }
                }
            } catch (e: Exception) {
                // Handle the error scenario
            }
        }
    }

    fun addRepoDetail(repoDetail : GitRepositoryDetail,startHomeFragment: (Unit) -> Unit ) {
        networkScope.launch {
            gitRepositoryService.addRepoDetail(repoDetail)
            withContext(Dispatchers.Main){
                startHomeFragment(Unit)
            }
        }
    }

    fun getRepositoryDetailFromDB(){
        networkScope.launch {
            try {
              val resultUser =   Utils.realmDefaultInstance { realm ->
                   return@realmDefaultInstance realm.evictResult(
                        realm.where(GitRepositoryDetail::class.java)
                            .findAll()
                    )
                }
                withContext(Dispatchers.Main) {
                   _repositoryDetailList.value = resultUser
                }
            } catch (e: Exception) {
                // Handle the error scenario
            }
        }

    }

    override fun onCleared() {
        super.onCleared()
        networkScope.cancel() // Cancel the coroutine scope when the ViewModel is cleared
    }
}