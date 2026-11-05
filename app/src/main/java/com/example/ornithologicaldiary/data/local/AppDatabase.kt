package com.example.ornithologicaldiary.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.ornithologicaldiary.data.local.dao.ObservationDao
import com.example.ornithologicaldiary.data.local.dao.SpeciesDao
import com.example.ornithologicaldiary.data.local.entity.ObservationEntity
import com.example.ornithologicaldiary.data.local.entity.SpeciesEntity

@Database(
    entities = [SpeciesEntity::class, ObservationEntity::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun speciesDao(): SpeciesDao

    abstract fun observationDao(): ObservationDao

    companion object {
        private const val DB_NAME = "ornithological-diary.db"

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE observations ADD COLUMN photoPath TEXT")
            }
        }

        private val defaultSpecies = listOf(
            "Большая синица" to "Parus major",
            "Сизый голубь" to "Columba livia",
            "Обыкновенный скворец" to "Sturnus vulgaris",
            "Серая ворона" to "Corvus cornix"
        )

        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            instance ?: synchronized(this) {
                instance ?: build(context.applicationContext).also { instance = it }
            }

        private fun build(context: Context): AppDatabase =
            Room.databaseBuilder(context, AppDatabase::class.java, DB_NAME)
                .addMigrations(MIGRATION_1_2)
                .addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        defaultSpecies.forEach { (name, latinName) ->
                            db.execSQL(
                                "INSERT INTO species (name, latinName) VALUES (?, ?)",
                                arrayOf(name, latinName)
                            )
                        }
                    }
                })
                .build()
    }
}
