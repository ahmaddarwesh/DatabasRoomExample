package myapp.com.testroom

import android.content.Context
import androidx.room.*


@Entity(tableName = "User")
data class User(
    @PrimaryKey(autoGenerate = true) var id: Int,
    @ColumnInfo(name = "username") var username: String,
    @ColumnInfo(name = "email") var email: String
)

@Dao
interface DaoUser {

    @Insert
    fun insert(user: User)

    @Query("Select * From User")
    fun AllUsers(): List<User>

    @Delete
    fun deleteAll(User: List<User>)

    @Query("Delete from User Where id LIKE :id")
    fun DeletebyId(id: Int)
}


@Database(entities = [User::class], version = 1, exportSchema = true)
abstract class DataRoom() : RoomDatabase() {
    abstract fun Dao(): DaoUser

    companion object {
        private var INSTANCE: DataRoom? = null
        fun getAppDatabase(context: Context): DataRoom {
            if (INSTANCE == null) {
                INSTANCE =
                    Room.databaseBuilder(context.applicationContext, DataRoom::class.java, "DataUser")
                        .allowMainThreadQueries().build()

            }
            return INSTANCE!!
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }


}

