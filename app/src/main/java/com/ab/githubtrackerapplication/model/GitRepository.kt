package com.ab.githubtrackerapplication.model

import com.google.gson.annotations.Expose
import io.realm.RealmObject
import io.realm.annotations.Ignore
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import java.util.*

@RealmClass
open class GitRepositoryDetail  : RealmObject() {
    @PrimaryKey
    @Expose open var id : Long = 0L
    @Expose open var owner : Owner?  = Owner()
    @Expose open var name : String = ""
    @Expose open var createdAt : Date = Date()
    @Expose open var htmlUrl : String = ""
    @Expose open var description : String? =null
}

@RealmClass
open class Owner : RealmObject(){
    @PrimaryKey
    @Expose open var  id : Long  = 0L
    @Expose open var login : String =""
    @Ignore
    @Expose open var avatarUrl : String = ""
}