package com.dearos.clickandrace

import android.app.Application
import com.dearos.clickandrace.di.viewModelModule
import com.dearos.clickandrace.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.android.ext.koin.androidLogger


/**
 * La clase `App` es la clase principal de la aplicación, que extiende de `Application`.
 * Esta clase se encarga de inicializar componentes globales de la aplicación, como Koin,
 * para la gestión de dependencias.
 *
 * Koin es un framework de inyección de dependencias que ayuda a gestionar las dependencias
 * de manera eficiente y sin necesidad de crear instancias manualmente.
 */
class App : Application() {

    /**
     * Este método es llamado cuando la aplicación es creada.
     * Aquí es donde inicializamos Koin y lo configuramos.
     *
     * @see startKoin
     */
    override fun onCreate() {
        super.onCreate()

        startKoin {

            /**
             * Activa el logger de Koin para mostrar información sobre la inicialización
             * y el proceso de inyección de dependencias en el logcat.
             */
            androidLogger()

            /**
             * Configura el contexto de la aplicación. Es necesario que Koin sepa cuál es
             * el contexto actual para poder acceder a los recursos del sistema Android.
             */
            androidContext(this@App)

            /**
             * Especifica los módulos que Koin debe cargar.
             * Los módulos son los lugares donde se definen las dependencias que la aplicación necesita.
             *
             * En este caso, estamos añadiendo dos módulos:
             * - `appModule`:
             *          Define las dependencias generales de la aplicación.
             * - `viewModelModule`:
             *          Define las dependencias para los ViewModels.
             */

            modules(appModule, viewModelModule)
        }
    }
}
