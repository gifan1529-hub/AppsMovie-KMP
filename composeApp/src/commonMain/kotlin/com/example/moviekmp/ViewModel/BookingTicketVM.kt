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
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

/**
 * data awal dari booking data
 */
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
    private val     _bookingData = MutableStateFlow<BookingData>(BookingData())
    val bookingData: StateFlow<BookingData> = _bookingData

    init {
        _buffetMenuList.value = buffetMenuUC()
    }

    /**
     * function onPaymentFinished untuk reset state paymentTrigger
     */
    fun onPaymentFinished() {
        paymentTrigger.value = false
    }

    /**
     *function ini untuk mneghitung harga dan validasi seat
     */
    private fun updateStateAndValidate(){
        val currentData = _bookingData.value ?: return
        // Ambil data tiket & makanan, trus dihitung harganya
        val newTotal = calculatePriceUC(
            currentData.adultTickets,
            currentData.childTickets,
            _buffetMenuList.value ?: emptyList()
        )
        // nyimpen hasil hitungan ke dalem bookingData
        val updatedData = currentData.copy(totalPrice = newTotal)
        _bookingData.value = updatedData
    }

    /**
     * ngambil daftar menu buffet yang di klik
     * ngitung harga sesuai sama quaintity nya
     * nyimpen hasil jumlah nya ke [buffetSubtotal]
     * manggil function [updateStateAndValidate] untuk update state nya
     */
    private fun updateBuffetAndRecalculate() {
        val currentData = _bookingData.value ?: return
        var buffetTotal = 0.0
        _buffetMenuList.value?.forEach { buffetItem ->
            buffetTotal += buffetItem.price * buffetItem.quantity
        }
        currentData.buffetSubtotal = buffetTotal
        updateStateAndValidate()
    }

    /**
     * function unutk ngurangin jumlah quantity buffet
     * bakal ngitung ulang total harga buffet nya
     */
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

    /**
     * function unutuk nambahin jumlah quantity buffet
     * bakal ngitung ulang total harga buffet nya
     */
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
    /**
     * function unutuk nambahin jumlah quantity ticket dewasa
     * bakal ngitung ulang total harga ticket nya
     */
    fun addAdultTicket() {
        val currentData = _bookingData.value ?: return
        currentData.adultTickets++
        updateStateAndValidate()
    }
    /**
     * function unutuk ngurangin jumlah quantity ticket dewasa
     * bakal ngitung ulang total harga ticket nya
     */
    fun removeAdultTicket() {
        val currentData = _bookingData.value ?: return
        if (currentData.adultTickets > 0) {
            currentData.adultTickets--
            updateStateAndValidate()
        }
    }
    /**
     * function unutuk nambahin jumlah quantity ticket bocil
     * bakal ngitung ulang total harga ticket nya
     */
    fun addChildTicket() {
        val currentData = _bookingData.value ?: return
        currentData.childTickets++
        updateStateAndValidate()
    }
    /**
     * function unutuk ngurangin jumlah quantity ticket bocil
     * bakal ngitung ulang total harga ticket nya
     */
    fun removeChildTicket() {
        val currentData = _bookingData.value ?: return
        if (currentData.childTickets > 0) {
            currentData.childTickets--
            updateStateAndValidate()
        }
    }

    /**
     * function ini untuk nyatet apa yang udah di pesan menjadi String untuk di tampilin di seatScreen
     * contoh : 2x Popcorn
     * kalo gada yang dipesen tulisannhya none
     */
    fun confirmBuffetSelection() {
        val currentBookingData = _bookingData.value ?: return
        val selectedBuffetItems = _buffetMenuList.value?.filter { it.quantity > 0 }
        currentBookingData.selectedBuffet = selectedBuffetItems?.joinToString("\n") {
            "${it.quantity}x ${it.name}"
        }?.takeIf { it.isNotBlank() } ?: "None"
        _bookingData.value = currentBookingData
    }

    /**
     * ngisi data awal film ke dalem state [bookingData]
     */
    fun setInitialMovieData(id: String?, title: String?, posterUrl: String?, genre: String?) {
        println("qwerty : VM $posterUrl")
        _bookingData.value = _bookingData.value.copy(
            movieId = id,
            movieTitle = title,
            moviePosterUrl = posterUrl,
            movieGenre = genre
        )
    }

    /**
     * myimpen teater yang dipilih
     * dan nandain kalo teater udah di pilih
     */
    fun setTheater(theaterName: String) {
        val currentData = _bookingData.value ?: return
        currentData.theater = theaterName
        _bookingData.value = currentData
        _isTheaterSelected.value = true
    }

    /**
     * myimpen session yang dipilih
     * dan nandain kalo session udah di pilih
     */
    fun setSession(sessionTime: String) {
        val currentData = _bookingData.value ?: return
        currentData.session = sessionTime
        _bookingData.value = currentData
        _isSessionSelected.value = true
    }

    /**
     * function untuk milih dan batalin pilihan kursi
     * mastiin jumlah kursi dan jumlah ticket sama
     */
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

    /**
     * ngereset data booking ke awal
     */
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

    /**
     * function untuk confirm payment dan dave ke database
     */
    @OptIn(ExperimentalTime::class, DelicateCoroutinesApi::class)
    fun confirmPaymentAndSave(method: String, userEmail: String) {
        println("ok: proses pembayaran")
        val data = _bookingData.value

        if (data == null || data.movieTitle == null) {
            println("gagal: data kosong")
            return
        }
        println("ok: Cek status scope: ${viewModelScope.isActive}")

        GlobalScope.launch {
            println("ok: masuk ke launch")
            try {
                    val currentTimestamp = Clock.System.now().toEpochMilliseconds()

                /**
                 * nymmpen data pesanan ke database booking history
                 */
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

                    println("ok: coba Insert ke Database")
                    val newId = BookingDao.insertBooking(history)
                    _lastInsertedId.value = newId.toInt()
                    println("ok:  ID: $newId")
                /**
                 * setelah selesai mesen akan munucl notification
                 */
                    NotificationHelper.showSuccessNotification(
                        movieTitle = data.movieTitle ?: "",
                        theater = data.theater ?: "",
                        bookingId = newId.toInt()
                    )
                /**
                 * setelah selesai mesen data nya akan di reset
                 * men cegah supaya data yang tadi sudah di pesan tidak muncul jika ingin mesen ticket lagi
                 */
                    onPaymentFinished()
                    resetBookingData()
                    println("ok: Proses Selesai.")
            } catch (e: Exception) {
                println("gagal: ${e.message}")
                e.printStackTrace()
                onPaymentFinished()
            }
        }
    }
}