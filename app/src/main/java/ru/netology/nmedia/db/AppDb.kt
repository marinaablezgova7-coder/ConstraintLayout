package ru.netology.nmedia.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.room3.Database
import androidx.room3.Room
import androidx.room3.RoomDatabase
import ru.netology.nmedia.dao.PostDao
import ru.netology.nmedia.entity.PostEntity

@Database(entities = [PostEntity::class], version = 1)
abstract class AppDb (): RoomDatabase() {

    abstract val postDao: PostDao

    companion object {
        @Volatile
        private var instance: AppDb? = null

        fun getInstance(context: Context): AppDb {
            return instance ?: synchronized(this) {
                instance ?:  buildDatabase(context)
                    .also { instance = it }
            }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context, AppDb::class.java, "app.db")
                .fallbackToDestructiveMigration(true)
                .allowMainThreadQueries()
                .build()
    }
}


class DbHelper(
    context: Context,
    dbVersion: Int,
    dbName: String,
    private val DDLs: Array<String>
) : SQLiteOpenHelper(
    context,
    dbName,
    null,
    dbVersion
) {

    override fun onCreate(db: SQLiteDatabase) {
        DDLs.forEach {
            db.execSQL(it)
        }
    }

    override fun onUpgrade(
        db: SQLiteDatabase,
        oldVersion: Int,
        newVersion: Int
    ) {
        if (oldVersion < 2) {
            db.execSQL(
                "ALTER TABLE posts ADD COLUMN shares INTEGER NOT NULL DEFAULT 0"
            )

            db.execSQL(
                "ALTER TABLE posts ADD COLUMN views INTEGER NOT NULL DEFAULT 0"
            )
        }

        if (oldVersion < 3) {
            db.execSQL(
                "ALTER TABLE posts ADD COLUMN sharesByMe INTEGER NOT NULL DEFAULT 0"
            )

            db.execSQL(
                "ALTER TABLE posts ADD COLUMN viewsByMe INTEGER NOT NULL DEFAULT 0"
            )

            db.execSQL(
                "ALTER TABLE posts ADD COLUMN video TEXT"
            )
        }
    }

    override fun onDowngrade(
        db: SQLiteDatabase,
        oldVersion: Int,
        newVersion: Int
    ) {
        // Ничего не делаем
    }
}