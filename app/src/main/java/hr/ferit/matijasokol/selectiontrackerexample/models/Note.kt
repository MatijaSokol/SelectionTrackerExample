package hr.ferit.matijasokol.selectiontrackerexample.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import hr.ferit.matijasokol.selectiontrackerexample.other.Constants.NOTES_TABLE_NAME

@Entity(tableName = NOTES_TABLE_NAME)
data class Note(
    val title: String,
    val description: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L
}