package com.example.alkoplan.data.user

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addUser(user: User)

    @Query("SELECT * FROM user_data ORDER BY id ASC")
    fun readAllData(): LiveData<List<User>>

    @Query("SELECT * FROM user_data WHERE userName = :userName")
    fun findUserByName(userName: String): List<User>

    @Query("SELECT * FROM user_data WHERE userName = :userName AND password = :password")
    fun findUser(userName: String, password: String): User

    @Query("UPDATE user_data SET is_LoggedIn = :status WHERE userName = :userName")
    fun updateUser(status: Boolean, userName: String)

    @Query("UPDATE user_data SET photoUrl = :uri WHERE userName = :userName")
    fun updateProfilePhotoUri(uri: String, userName: String)

    @Query("SELECT * FROM user_data WHERE is_LoggedIn = :status")
    fun checkIfUserIsCurrent(status: Boolean): User

    @Query("UPDATE user_data SET is_LoggedIn = :status WHERE is_LoggedIn = :status2")
    fun logoutAll(status: Boolean, status2:Boolean)
}