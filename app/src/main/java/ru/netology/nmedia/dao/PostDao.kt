package ru.netology.nmedia.dao

import androidx.room3.Dao
import androidx.room3.Query
import androidx.room3.Upsert
import ru.netology.nmedia.entity.PostEntity

@Dao
interface PostDao {

    @Query("SELECT * FROM PostEntity ORDER BY id DESC")
    fun getAll(): List<PostEntity>

    @Upsert
    fun save(post: PostEntity): Long

    @Query("""
        UPDATE PostEntity SET
        likes = likes + CASE WHEN likedByMe THEN -1 ELSE 1 END,
        likedByMe = CASE WHEN likedByMe THEN 0 ELSE 1 END
        WHERE id = :id
    """)
    fun likeById(id: Long)

    @Query("""
        UPDATE PostEntity SET
        shares = shares + 1,
        sharesByMe = 1
        WHERE id = :id
    """)
    fun shareById(id: Long)

    @Query("""
        UPDATE PostEntity SET
        views = views + 1,
        viewsByMe = 1
        WHERE id = :id
    """)
    fun viewsById(id: Long)

    @Query("DELETE FROM PostEntity WHERE id = :id")
    fun removeById(id: Long)
}