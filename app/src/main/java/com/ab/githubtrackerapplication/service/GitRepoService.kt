package com.ab.githubtrackerapplication.service

import com.ab.githubtrackerapplication.api.RetrofitWrapper
import com.ab.githubtrackerapplication.model.GitRepositoryDetail
import com.ab.githubtrackerapplication.util.Utils

class GitRepoService  {

    private val gitRepoService = RetrofitWrapper.gitRepoApiInterface


    suspend fun getRepositoryDetail(owner : String,repoName : String): Result<GitRepositoryDetail> {
        return  kotlin.runCatching {
            gitRepoService.getRepositoryDetail(owner,repoName)
        }
    }

    fun addRepoDetail(repositoryDetail: GitRepositoryDetail) {
        Utils.realmDefaultInstance { realm ->
            realm.executeTransaction {
                realm.copyToRealmOrUpdate(repositoryDetail)
            }
        }
    }
}
