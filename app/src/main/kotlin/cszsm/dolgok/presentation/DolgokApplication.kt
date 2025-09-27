package cszsm.dolgok.presentation

import android.app.Application
import cszsm.dolgok.domain.di.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class DolgokApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@DolgokApplication)
            modules(appModules)
        }
    }
}