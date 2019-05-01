package com.vladislavmyasnikov.courseproject.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vladislavmyasnikov.courseproject.data.db.entities.LectureEntity

@Dao
interface LectureDao {

    @Query("SELECT * FROM lectures ORDER BY id")
    fun loadLectures(): List<LectureEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLectures(lectures: List<LectureEntity>)
}
