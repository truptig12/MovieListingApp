package com.frogsocial.auth_data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.frogsocial.auth_data.local.entity.UserEntity

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(users: UserEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUserAll(users: List<UserEntity>): List<Long>

    //checking user exist or not in our db
    @Query("SELECT * FROM UserEntity WHERE email LIKE :email AND password LIKE :password")
    fun readLoginData(email: String, password: String): UserEntity


    //getting user data details
    @Query("select * from userentity where id Like :id")
    fun getUserDataDetails(id: Long): UserEntity

    //deleting all user from db
    @Query("DELETE FROM UserEntity")
    fun deleteAll()
}