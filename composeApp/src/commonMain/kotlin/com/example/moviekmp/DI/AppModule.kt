package com.example.moviekmp.DI

import com.example.moviekmp.Data.Impl.BookingHistoryRepositoryImpl
import com.example.moviekmp.Data.Impl.DetailTicketImpl
import com.example.moviekmp.Data.Impl.EditUserRepositoryImpl
import com.example.moviekmp.Data.Impl.MovieRepositoryImpl
import com.example.moviekmp.Data.Impl.UserRepositoryImpl
import com.example.moviekmp.Data.Local.AppDatabase
import com.example.moviekmp.Data.Local.BookingHistoryDao
import com.example.moviekmp.Data.Local.FavoriteMovieDao
import com.example.moviekmp.Data.Local.PrefsManager
import com.example.moviekmp.Data.Local.RoomDao
import com.example.moviekmp.Data.Local.UserDao
import com.example.moviekmp.Data.Local.createDataStore
import com.example.moviekmp.Data.Remote.ApiService
import com.example.moviekmp.Domain.Repository.BookingHistoryRepository
import com.example.moviekmp.Domain.Repository.DetailTicketRepository
import com.example.moviekmp.Domain.Repository.EditUserRepository
import com.example.moviekmp.Domain.Repository.MovieRepository
import com.example.moviekmp.Domain.Repository.UserRepository
import com.example.moviekmp.Domain.Usecase.BuffetMenuUC
import com.example.moviekmp.Domain.Usecase.CalculatePriceUC
import com.example.moviekmp.Domain.Usecase.GetBookingDetailUC
import com.example.moviekmp.Domain.Usecase.GetBookingUC
import com.example.moviekmp.Domain.Usecase.GetMovieDetailUC
import com.example.moviekmp.Domain.Usecase.GetMovieUC
import com.example.moviekmp.Domain.Usecase.GetUserDetailsUC
import com.example.moviekmp.Domain.Usecase.GetUserUC
import com.example.moviekmp.Domain.Usecase.LoginUC
import com.example.moviekmp.Domain.Usecase.LogoutUserUC
import com.example.moviekmp.Domain.Usecase.RefreshMovieUC
import com.example.moviekmp.Domain.Usecase.RegisterUserUC
import com.example.moviekmp.Domain.Usecase.SearchUC
import com.example.moviekmp.Domain.Usecase.UpdateUserUC
import com.example.moviekmp.Domain.Usecase.ValidateSeatUC
import com.example.moviekmp.ViewModel.AuthViewModel
import com.example.moviekmp.ViewModel.BookingTicketVM
import com.example.moviekmp.ViewModel.DetailFilmVM
import com.example.moviekmp.ViewModel.DetailTicketVM
import com.example.moviekmp.ViewModel.EditUserVM
import com.example.moviekmp.ViewModel.FavoriteVM
import com.example.moviekmp.ViewModel.MovieRepositoryVM
import com.example.moviekmp.ViewModel.SearchVM
import com.example.moviekmp.ViewModel.SignInVM
import com.example.moviekmp.ViewModel.SignUpVM
import com.example.moviekmp.ViewModel.TicketVM
import com.example.moviekmp.ViewModel.UserVM
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

expect val platformModule: org.koin.core.module.Module
// DI
/**
 * buat ngatur dependency injection nya
 * ibarat binds dan provides kalo di compose
 * factory kalo di xml
 */
val appModule = module {
    /**
     * single : akan aktif terus selama app nya belum ditutup
     * factory : hanya aktif ketika di panggil saja
     * viewModel : sama aja kaya factory
     */
    single { PrefsManager(get()) }

    // Dao
    single <UserDao> { get<AppDatabase>().userDao() }
    single <RoomDao>  { get<AppDatabase>().roomDao() }
    single <FavoriteMovieDao> { get<AppDatabase>().movieDao() }
    single <BookingHistoryDao> { get<AppDatabase>().bookingHistoryDao() }

    // Repository
    single<BookingHistoryRepository> { BookingHistoryRepositoryImpl(get()) }
    single<DetailTicketRepository> { DetailTicketImpl(get()) }
    single<EditUserRepository> { EditUserRepositoryImpl(get()) }
    single<UserRepository> { UserRepositoryImpl(get()) }
    single<MovieRepository> { MovieRepositoryImpl(
        apiService = get(),
        movieDao = get<RoomDao>(),
        dao = get<FavoriteMovieDao>()
    ) }

    single { ApiService(get()) }

    // Network
    single {
        HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                })
            }
            install(Logging) {
                level = LogLevel.ALL
            }
        }
    }

    // Usecase
//    factory { LoginUC(get()) }
//    factory { GetMovieUC(get()) }
//    factory { RefreshMovieUC(get()) }
//    factory { RegisterUserUC(get()) }
//    factory { UpdateUserUC(get()) }
//    factory { SearchUC(get()) }
//    factory { GetMovieDetailUC(get()) }
//    factory { ValidateSeatUC() }
//    factory { CalculatePriceUC() }
//    factory { GetBookingDetailUC(get()) }
//    factory { GetBookingUC(get()) }
//    factory { GetUserDetailsUC(get()) }
//    factory { GetUserUC(get()) }
//    factory { BuffetMenuUC() }
//    factory { LogoutUserUC(get()) }

//    single { LoginUC(get()) }
//    single { GetMovieUC(get()) }
//    single { RefreshMovieUC(get()) }
//    single { RegisterUserUC(get()) }
//    single { UpdateUserUC(get()) }
//    single { SearchUC(get()) }
//    single { GetMovieDetailUC(get()) }
//    single { ValidateSeatUC() }
//    single { CalculatePriceUC() }
//    single { GetBookingDetailUC(get()) }
//    single { GetBookingUC(get()) }
//    single { GetUserDetailsUC(get()) }
//    single { GetUserUC(get()) }
//    single { BuffetMenuUC() }
//    single { LogoutUserUC(get()) }

    factoryOf(::LoginUC)
    factoryOf(::GetMovieUC)
    factoryOf(::RefreshMovieUC)
    factoryOf(::RegisterUserUC)
    factoryOf(::UpdateUserUC)
    factoryOf(::SearchUC)
    factoryOf(::GetMovieDetailUC)
    factoryOf(::ValidateSeatUC)
    factoryOf(::CalculatePriceUC)
    factoryOf(::GetBookingDetailUC)
    factoryOf(::GetBookingUC)
    factoryOf(::GetUserDetailsUC)
    factoryOf(::GetUserUC)
    factoryOf(::BuffetMenuUC)
    factoryOf(::LogoutUserUC)

    // ViewModel
//    single { SignInVM(get(), get()) }
//    single { SignUpVM(get()) }
//    single { AuthViewModel(get()) }
//    single { BookingTicketVM(get(), get(), get(), get()) }
//    single { DetailFilmVM(get(), get(), get(), get()) }
//    single { DetailTicketVM(get()) }
//    single { EditUserVM(get(), get(), get()) }
//    single { FavoriteVM(get(),get()) }
//    single { MovieRepositoryVM(get(), get()) }
//    single { SearchVM(get()) }
//    single { TicketVM(get(), get()) }
//    single { UserVM(get(), get(), get()) }

//    factory { SignInVM(get(), get()) }
//    factory { SignUpVM(get()) }
//    factory { AuthViewModel(get()) }
//    factory { BookingTicketVM(get(), get(), get(), get()) }
//    factory { DetailFilmVM(get(), get(), get(), get()) }
//    factory { DetailTicketVM(get()) }
//    factory { EditUserVM(get(), get(), get()) }
//    factory { FavoriteVM(get(),get()) }
//    factory { MovieRepositoryVM(get(), get()) }
//    factory { SearchVM(get()) }
//    factory { TicketVM(get(), get()) }
//    factory { UserVM(get(), get(), get()) }

    viewModel { SignInVM(get(), get()) }
    viewModel { SignUpVM(get()) }
    viewModel { AuthViewModel(get())  }
    single { BookingTicketVM(get(), get(), get(), get(), get()) }
    viewModel { DetailFilmVM(get(), get(), get(), get()) }
    viewModel { DetailTicketVM(get()) }
    viewModel { EditUserVM(get(), get(), get()) }
    viewModel { FavoriteVM(get(),get()) }
    viewModel { MovieRepositoryVM(get(), get()) }
    viewModel { SearchVM(get()) }
    viewModel { TicketVM(get(), get()) }
    viewModel { UserVM(get(), get(), get()) }

}