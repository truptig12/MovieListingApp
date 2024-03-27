package com.frogsocial.auth_data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.frogsocial.auth_data.local.entity.UserEntity

@Database(
    entities = [UserEntity::class],
    version = 1
)
abstract class UserDatabase : RoomDatabase(){

    abstract val dao : UserDao
}