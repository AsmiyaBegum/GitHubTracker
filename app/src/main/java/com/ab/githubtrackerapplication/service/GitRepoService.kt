package com.ab.githubtrackerapplication.service

import com.ab.githubtrackerapplication.api.RetrofitWrapper
import com.ab.githubtrackerapplication.gitRepo.GitRepoViewModel
import com.ab.githubtrackerapplication.model.GitRespositoryDetail

class GitRepoService  {

    private val gitRepoService = RetrofitWrapper.gitRepoApiInterface


    suspend fun getRepositoryDetail(owner : String,repoName : String): Result<GitRespositoryDetail> {
        return  kotlin.runCatching {
            gitRepoService.getRepositoryDetail(owner,repoName)
        }
    }
}
