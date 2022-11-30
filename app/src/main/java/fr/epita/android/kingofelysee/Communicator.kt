package fr.epita.android.kingofelysee
import android.R
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import fr.epita.android.kingofelysee.objects.Character

interface Communicator {
    fun passCharacterToFragment(id: Int, i: Int)

    fun loadShopFragment()

    fun unloadFragment()

    fun loadMyCardsFragment()

    fun loadDiceFragment()

    fun loadMap()

    fun dialog(message : String, title : String)

}