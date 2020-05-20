package com.techm.roomdemo.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface UserDAO {

    @Query("SELECT * FROM user_data_table")
    fun getAllUsers(): LiveData<List<User>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User) : Long

    @Update
    suspend fun updateUser(user: User) : Int

    @Delete
    suspend fun deleteUser(user: User) : Int

    @Query("DELETE FROM user_data_table")
    suspend fun deleteAll() : Int


}