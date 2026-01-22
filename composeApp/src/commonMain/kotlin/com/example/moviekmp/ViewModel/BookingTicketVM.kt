package com.example.moviekmp.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviekmp.DI.NotificationHelper
import com.example.moviekmp.DI.NotificationHelper.showSuccessNotification
import com.example.moviekmp.Data.Local.BookingHistoryDao
import com.example.moviekmp.Data.Local.PrefsManager
import com.example.moviekmp.Domain.Model.BookingHistory
import com.example.moviekmp.Domain.Usecase.BuffetItem
import com.example.moviekmp.Domain.Usecase.BuffetMenuUC
import com.example.moviekmp.Domain.Usecase.CalculatePriceUC
import com.example.moviekmp.Domain.Usecase.GetUserUC
import com.example.moviekmp.Domain.Usecase.ValidateSeatUC
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

data class BookingData(
    var movieId: String? = null,
    var movieTitle: String? = null,
    var moviePosterUrl: String? = null,
    var movieGenre: String? = null,
    var theater: String? = null,
    var session: String? = null,
    var adultTickets: Int = 0,
    var childTickets: Int = 0,
    var buffetSubtotal: Double = 0.0,
    var selectedSeats: MutableSet<Int> = mutableSetOf(),
    var selectedBuffet: String? = "None",
    var totalPrice: Double = 0.0
)

class BookingTicketVM (
    private val BookingDao: BookingHistoryDao,
    private val validateSeatUC: ValidateSeatUC,
    private val calculatePriceUC: CalculatePriceUC,
    private val buffetMenuUC: BuffetMenuUC,
    private val prefsManager: PrefsManager
) : ViewModel() {
    private val _userEmail = MutableStateFlow("")
    private val _lastInsertedId = MutableStateFlow<Int?>(null)
    val lastInsertedId: StateFlow<Int?> = _lastInsertedId

    private val _areSeatsValid = MutableStateFlow<Boolean>(false)
    val areSeatsValid: StateFlow<Boolean> get() = _areSeatsValid

    private val _isTheaterSelected = MutableStateFlow<Boolean>(false)
    val isTheaterSelected: StateFlow<Boolean> get() = _isTheaterSelected

    private val _isSessionSelected = MutableStateFlow<Boolean>(false)
    val isSessionSelected: StateFlow<Boolean> get() = _isSessionSelected

    private val _buffetMenuList = MutableStateFlow<List<BuffetItem>>(emptyList())
    val buffetMenuList: StateFlow<List<BuffetItem>> = _buffetMenuList
    val paymentTrigger = MutableStateFlow(false)
    private val _takenSeats = MutableStateFlow<Set<String>>(emptySet())
    val takenSeats: StateFlow<Set<String>> = _takenSeats
    private val _bookingData = MutableStateFlow<BookingData>(BookingData())
    val bookingData: StateFlow<BookingData> = _bookingData

    init {
//        _bookingData.value = BookingData()
        _buffetMenuList.value = buffetMenuUC()
    }

    fun onConfirmPaymentClicked() {
        paymentTrigger.value = true
    }

    fun onPaymentFinished() {
        paymentTrigger.value = false
    }

    private fun updateStateAndValidate(){
        val currentData = _bookingData.value ?: return

        val newTotal = calculatePriceUC(
            currentData.adultTickets,
            currentData.childTickets,
            _buffetMenuList.value ?: emptyList()
        )

        val updatedData = currentData.copy(totalPrice = newTotal)
        _bookingData.value = updatedData
    }

    private fun updateBuffetAndRecalculate() {
        val currentData = _bookingData.value ?: return
        var buffetTotal = 0.0
        _buffetMenuList.value?.forEach { buffetItem ->
            buffetTotal += buffetItem.price * buffetItem.quantity
        }
        currentData.buffetSubtotal = buffetTotal
        updateStateAndValidate()
    }

    fun removeBuffetItem(item: BuffetItem) {
        val currentList = _buffetMenuList.value?.toMutableList() ?: return
        val index = currentList.indexOfFirst { it.id == item.id }
        if (index != -1 && currentList[index].quantity > 0) {
            val updatedItem = currentList[index].copy(quantity = currentList[index].quantity - 1)
            currentList[index] = updatedItem

            _buffetMenuList.value = currentList
            updateBuffetAndRecalculate()
        }
    }

    fun addBuffetItem(item: BuffetItem) {
        val currentList = _buffetMenuList.value?.toMutableList() ?: return
        val index = currentList.indexOfFirst { it.id == item.id }

        if (index != -1) {
            val updatedItem = currentList[index].copy(quantity = currentList[index].quantity + 1)
            currentList[index] = updatedItem

            _buffetMenuList.value = currentList
            updateBuffetAndRecalculate()
        }
    }

    fun addAdultTicket() {
        val currentData = _bookingData.value ?: return
        currentData.adultTickets++
        updateStateAndValidate()
    }

    fun removeAdultTicket() {
        val currentData = _bookingData.value ?: return
        if (currentData.adultTickets > 0) {
            currentData.adultTickets--
            updateStateAndValidate()
        }
    }

    fun addChildTicket() {
        val currentData = _bookingData.value ?: return
        currentData.childTickets++
        updateStateAndValidate()
    }

    fun removeChildTicket() {
        val currentData = _bookingData.value ?: return
        if (currentData.childTickets > 0) {
            currentData.childTickets--
            updateStateAndValidate()
        }
    }

    fun confirmBuffetSelection() {
        val currentBookingData = _bookingData.value ?: return
        val selectedBuffetItems = _buffetMenuList.value?.filter { it.quantity > 0 }
        currentBookingData.selectedBuffet = selectedBuffetItems?.joinToString("\n") {
            "${it.quantity}x ${it.name}"
        }?.takeIf { it.isNotBlank() } ?: "None"
        _bookingData.value = currentBookingData
    }

    fun setInitialMovieData(id: String?, title: String?, posterUrl: String?, genre: String?) {
        println("qwerty : VM $posterUrl")
        _bookingData.value = _bookingData.value.copy(
            movieId = id,
            movieTitle = title,
            moviePosterUrl = posterUrl,
            movieGenre = genre
        )
    }

    fun setTheater(theaterName: String) {
        val currentData = _bookingData.value ?: return
        currentData.theater = theaterName
        _bookingData.value = currentData
        _isTheaterSelected.value = true
    }


    fun setSession(sessionTime: String) {
        val currentData = _bookingData.value ?: return
        currentData.session = sessionTime
        _bookingData.value = currentData
        _isSessionSelected.value = true
    }

    fun onSeatSelected(seatIndex: Int) {
        val currentData = _bookingData.value ?: return
        val totalTickets = currentData.adultTickets + currentData.childTickets
        val newSelectedSeats = currentData.selectedSeats.toMutableSet()
        if (newSelectedSeats.contains(seatIndex)) {
            newSelectedSeats.remove(seatIndex)
        } else {
            if (newSelectedSeats.size < totalTickets) {
                newSelectedSeats.add(seatIndex)
            }
        }
        _bookingData.value = currentData.copy(selectedSeats = newSelectedSeats)
    }

    fun resetBookingData() {
        _bookingData.value = BookingData()
        _isSessionSelected.value = false
        _isTheaterSelected.value = false
        _areSeatsValid.value = false
    }

    fun fetchTakenSeats(movieId: String, theater: String, session: String) {
        viewModelScope.launch {
            val listOfSeatLists = BookingDao.getTakenSeatsForShow(movieId, theater, session)
            val allTakenSeats = listOfSeatLists.flatMap { it.split(",") }.toSet()
            _takenSeats.value = allTakenSeats
        }
    }

    @OptIn(ExperimentalTime::class)
    fun confirmPaymentAndSave(method: String, userEmail: String) {
        val data = _bookingData.value ?: return

        viewModelScope.launch {
            try {
                val currentTimestamp = Clock.System.now().toEpochMilliseconds()
                val email = prefsManager.getUserEmail().first()

                val history = BookingHistory(
                    movieTitle = data.movieTitle ?: "Unknown Movie",
                    theater = data.theater ?: "Unknown Theater",
                    session = data.session ?: "Unknown Session",
                    seatIds = data.selectedSeats.toList().map { it.toString() },
                    totalPrice = data.totalPrice.toLong(),
                    paymentMethod = method,
                    paymentStatus = "LUNAS",
                    id = 0,
                    email = userEmail,
                    buffetItems = data.selectedBuffet ?: "None",
                    adultTickets = data.adultTickets,
                    childTickets = data.childTickets,
                    moviePosterUrl = data.moviePosterUrl ?: "",
                    bookingDate = currentTimestamp
                )
                println("udah ${data.movieTitle} ${currentTimestamp} ${email}")

                val newId = BookingDao.insertBooking(history)
                _lastInsertedId.value = newId.toInt()

//                BookingDao.insertBooking(history)

                NotificationHelper.showSuccessNotification(
                    movieTitle = data.movieTitle ?: "",
                    theater = data.theater ?: "",
                    bookingId = newId.toInt()
                )

                onPaymentFinished()
                resetBookingData()

            } catch (e: Exception) {
                paymentTrigger.value = false
//                Log.e("BookingVM", "Failed to save booking: ${e.message}")
            }
        }
    }

    fun clearLastInsertedId() {
        _lastInsertedId.value = null
    }

//    private fun showSuccessNotification(movieTitle: String, theater: String, bookingId: Int) {
//        Log.d("Notification", "${movieTitle}")
//        val notificationManager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
//
//        val routeUri = "app://movie/detailticket/$bookingId".toUri()
//
//        val intent = Intent(
//            Intent.ACTION_VIEW,
//            routeUri,
//            context,
//            MainActivity::class.java
//        )
//
//        val pendingIntent = TaskStackBuilder.create(context).run {
//            addNextIntentWithParentStack(intent)
//            getPendingIntent(
//                bookingId,
//                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
//            )
//        }
//
//        val notification = NotificationCompat.Builder(context, "PYAMENT_SUCCES_CHANNEL")
//            .setSmallIcon(com.example.compose.R.drawable.ticket)
//            .setContentTitle("Pembayaran Berhasil! 🍿")
//            .setContentText("Tiket untuk $movieTitle di $theater berhasil dipesan.")
//            .setPriority(NotificationCompat.PRIORITY_HIGH)
//            .setContentIntent(pendingIntent)
//            .setAutoCancel(true)
//            .build()
//
//        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
//    }
}