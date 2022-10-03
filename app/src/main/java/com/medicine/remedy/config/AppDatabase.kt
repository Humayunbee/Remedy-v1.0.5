package com.medicine.remedy.config

import androidx.room.Database
import androidx.room.RoomDatabase
import com.medicine.remedy.database.dao.CartItemDao
import com.medicine.remedy.database.entities.CartItemEntities

/**
 * Date 2/4/2021.
 * Created by Md Atik Faysal(Android Developer)
 *
 */
@Database(
    entities = [CartItemEntities::class],
    version = 8
)
abstract class AppDatabase : RoomDatabase()
{
    abstract fun cartItemDao() : CartItemDao
}