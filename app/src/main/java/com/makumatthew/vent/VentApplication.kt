package ke.kazinow.app

import android.app.Application
import com.makumatthew.vent.utils.PrefManager
import com.makumatthew.vent.utils.ReleaseLogTree
import timber.log.Timber
import java.lang.Boolean

val DEBUG = Boolean.parseBoolean("true")

class VentApplication: Application() {

    companion object {
        // Singleton instance
        // Getter to access Singleton instance
        var instance: VentApplication? = null
            private set
    }

    val prefManager: PrefManager
        get() {
            return PrefManager.getInstance(this)!!
        }

    override fun onCreate() {
        super.onCreate()

        // Setup singleton instance
        instance = this

        //plant timber
        if (DEBUG) {
            Timber.plant(object : Timber.DebugTree() {
                //Add the line number to the tag
                override fun createStackElementTag(element: StackTraceElement): String? {
                    return super.createStackElementTag(element) + ": " + element.lineNumber
                }
            })
        } else {
            //Release mode
            Timber.plant(ReleaseLogTree())
        }

    }

    override fun onLowMemory() {
        super.onLowMemory()
    }
}
