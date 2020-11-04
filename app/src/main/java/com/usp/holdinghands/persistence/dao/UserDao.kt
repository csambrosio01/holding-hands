package com.usp.holdinghands.persistence.dao

import androidx.room.Dao
import com.usp.holdinghands.model.User

@Dao
abstract class UserDao : BaseDao<User>("user")
