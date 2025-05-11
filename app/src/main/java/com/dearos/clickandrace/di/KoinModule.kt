package com.dearos.clickandrace.di



import com.dearos.clickandrace.auth.domain.appRepository.UserRepository
import com.dearos.clickandrace.auth.domain.authRepository.LoginRepo
import com.dearos.clickandrace.auth.domain.authRepository.ResetPasswordRepo
import com.dearos.clickandrace.auth.domain.authRepository.SetPasswordRepo
import com.dearos.clickandrace.auth.domain.authRepository.SignUpRepo
import com.dearos.clickandrace.auth.domain.supabase.SupabaseClientProvider
import com.dearos.clickandrace.auth.domain.use_case.ValidateEmail
import com.dearos.clickandrace.auth.domain.use_case.ValidateName
import com.dearos.clickandrace.auth.domain.use_case.ValidatePassword
import com.dearos.clickandrace.auth.domain.use_case.ValidateTerms
import com.dearos.clickandrace.ui.screens.appScreens.homePageAuthTest.HomeViewModel
import com.dearos.clickandrace.ui.screens.appScreens.user.UserViewModel
import com.dearos.clickandrace.ui.screens.authScreens.login.LoginViewModel
import com.dearos.clickandrace.ui.screens.main.MainViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module


/**
 * - This module is used to define dependencies that are instances of normal classes
 * - Register all dependencies your ViewModels need.
 */
/**
 * appModule define todas las dependencias básicas de la aplicación
 * que no son ViewModels.
 *
 * Contiene:
 * - Cliente Supabase
 * - Repositorios
 * - Casos de uso de validaciones
 */
val appModule = module {
    /**
     * Registra el cliente de Supabase como un singleton.
     * Solo existirá una instancia durante toda la vida de la aplicación.
     */
    single { SupabaseClientProvider.client }

    /**
     * Registra los repositorios como singletons.
     * Cada uno recibe el cliente de Supabase automáticamente mediante `get()`.
     */
    single { SignUpRepo(get()) }
    single { ResetPasswordRepo(get()) }
    single { SetPasswordRepo(get()) }

    /**
     * Registra el repositorio de Login.
     * Necesita además el contexto de Android (`androidContext()`).
     */
    single { LoginRepo(androidContext(), get()) }

    single { UserRepository(get()) }



    /**
     * Registra los casos de uso de validación.
     * Se crean nuevas instancias cada vez que se necesiten (`factoryOf`).
     */
    factoryOf(::ValidateEmail)
    factoryOf(::ValidatePassword)
    factoryOf(::ValidateTerms)
    factoryOf(::ValidateName)




}

/**
 * viewModelModule define todas las dependencias de tipo ViewModel
 * que usa la aplicación.
 *
 * Cada ViewModel se registra para ser inyectado cuando se necesite
 * en pantallas o fragmentos.
 */
val viewModelModule = module {
    /**Auth**/

    /** ViewModel para la pantalla de registro de usuario */
 //  viewModelOf(::SignUpViewModel)

    /** ViewModel para la verificación de OTP */
   // viewModelOf(::OtpViewModel)

    /** ViewModel para la pantalla de inicio de sesión */
    viewModelOf(::LoginViewModel)

    /** ViewModel para la página principal tras autenticación */
    viewModelOf(::HomeViewModel)

    /** ViewModel para manejar el flujo principal de autenticación */
    viewModelOf(::MainViewModel)

    /** ViewModel para resetear la contraseña (olvido de contraseña) */
   // viewModelOf(::ResetPasswordViewModel)

    /** ViewModel para establecer una nueva contraseña */
  //  viewModelOf(::SetPasswordViewModel)

    /**App**/
    //viewModelOf(::UserViewModel) // << ¡Asegúrate de tener esta línea!

    viewModel {
        UserViewModel(
            userRepository = get(), // Repositorio de usuarios
            authRepo = get()        // Repositorio de autenticación
        )
    }



}