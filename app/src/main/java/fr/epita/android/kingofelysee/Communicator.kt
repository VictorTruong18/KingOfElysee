package fr.epita.android.kingofelysee
import fr.epita.android.kingofelysee.objects.Character

interface Communicator {
    fun passCharacterToFragment(id: Int, i: Int)

    fun loadShopFragment()

    fun unloadShopFragment()

    fun loadMyCardsFragment()

    fun loadMap()
}