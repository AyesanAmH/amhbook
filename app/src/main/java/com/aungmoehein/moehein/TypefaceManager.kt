package android.myanmarlinks.myanmarmps.helper.com.aungmoehein.moehein

import android.content.res.AssetManager
import android.graphics.Typeface
import android.util.LruCache

class TypefaceManager(private val manager: AssetManager) {
    companion object {
        const val PYIDAUNGSU = "pyidaungsu.ttf"
        const val ZAWGYIONE = "zawgyi_one.ttf"
        const val MYANMAR_SAGAR = "myanmar_sagar.ttf"
    }
    // Property with lazy delegate to be initialized only once when getTypeFace() is called for the first time
    private val mCache: LruCache<String, Typeface> by lazy {
        val cacheSize = 4 * 1024 * 1024
        LruCache<String, Typeface>(cacheSize)
    }
    // Traditional get() methods are replaced with immutable properties
    val sagar get() = getTypeface(MYANMAR_SAGAR)
    val uni get() = getTypeface(PYIDAUNGSU)
    val zawgyi get() = getTypeface(ZAWGYIONE)

    private fun getTypeface(filename: String): Typeface {
        var typeface: Typeface? = mCache.get(filename)
        return if (typeface != null) { // already in cache
            typeface
        } else { // create a new one from asset folder and put into cache
            typeface = Typeface.createFromAsset(manager, filename)
            mCache.put(filename, typeface)
            typeface
        }
    }
}