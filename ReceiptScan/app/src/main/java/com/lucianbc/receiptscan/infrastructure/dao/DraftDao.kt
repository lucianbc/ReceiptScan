package com.lucianbc.receiptscan.infrastructure.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lucianbc.receiptscan.domain.model.Annotation
import com.lucianbc.receiptscan.domain.model.Draft
import com.lucianbc.receiptscan.domain.model.DraftItem
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface DraftDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(draft: Draft): Single<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(annotations: List<Annotation>): Single<List<Long>>

    @Query("select id, creationTimestamp from draft")
    fun getDraftItems(): Flowable<List<DraftItem>>
}