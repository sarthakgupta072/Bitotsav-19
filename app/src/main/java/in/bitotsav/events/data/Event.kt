package `in`.bitotsav.events.data

import `in`.bitotsav.shared.Singleton
import android.content.Context
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import java.util.*

@Entity
data class Event(
    @PrimaryKey @SerializedName("eventId") val id: Int,
    @SerializedName("eventName") val name: String,
    @SerializedName("eventVenue") val venue: String,
    @SerializedName("eventTime") val timeString: String,
    @SerializedName("eventDay") val day: Int,
    @SerializedName("eventDescription") val description: String,
    @SerializedName("eventRules") val rules: String,
    @SerializedName("eventContact1Name") val contact1Name: String,
    @SerializedName("eventContact1Number") val contact1Number: Long,
    @SerializedName("eventContact2Name") val contact2Name: String,
    @SerializedName("eventContact2Number") val contact2Number: Long,
    @SerializedName("eventRequirement") val requirement: String,
    @SerializedName("eventPoints1") val points1: Int,
    @SerializedName("eventPoints2") val points2: Int,
    @SerializedName("eventPoints3") val points3: Int,
    // Can be "F" or "NF" for flagship and non-flagship respectively
    @SerializedName("eventType") val type: String,
    @SerializedName("eventMinimumMembers") val minimumMembers: Int,
    @SerializedName("eventMaximumMembers") val maximumMembers: Int,
    @SerializedName("eventCategory") val category: String,
    @SerializedName("eventStatus") val status: String,
    @SerializedName("eventPrize1") val prize1: Int,
    @SerializedName("eventPrize2") val prize2: Int,
    @SerializedName("eventPrize3") val prize3: Int,
//    TODO: Set type as List<String>? Confirm default value stored in db
    @SerializedName("eventPosition1") val position1: String = "NA",
    @SerializedName("eventPosition2") val position2: String = "NA",
    @SerializedName("eventPosition3") val position3: String = "NA"
) {
    // Using @Transient also makes room ignore the property
    @Expose(serialize = false, deserialize = false)
    var gregorianCalendar = getGregorianCalendarFromString(day, timeString)

    @Expose(serialize = false, deserialize = false)
    var isStarred: Boolean = false

    private fun getGregorianCalendarFromString(day: Int, timeString: String): GregorianCalendar {
        val (hours, minutes) = timeString.split(":").map { it.toInt() }
        return GregorianCalendar(2019, 2, day + 14, hours, minutes)
    }

    fun toggleStarred(context: Context) {
        isStarred = isStarred.not()
        CoroutineScope(Dispatchers.IO).async {
            EventRepository(Singleton.database.getInstance(context).eventDao()).insert(this@Event)
        }
    }
}