package com.lucianbc.receiptscan.infrastructure.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.lucianbc.receiptscan.domain.model.Annotation
import com.lucianbc.receiptscan.domain.model.Draft
import io.reactivex.Single

@Dao
interface DraftDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(draft: Draft): Single<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(annotations: List<Annotation>): Single<List<Long>>

}