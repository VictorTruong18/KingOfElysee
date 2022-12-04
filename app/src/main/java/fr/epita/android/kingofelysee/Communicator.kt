package fr.epita.android.kingofelysee

import fr.epita.android.kingofelysee.objects.Card
import fr.epita.android.kingofelysee.objects.Character

interface Communicator {
    fun passCharacterToFragment(id: Int, i: Int)

    fun loadShopFragment()

    fun unloadFragment()

    fun loadMyCardsFragment()

    fun loadDiceFragment()

    fun toggleShopBtn()

    fun loadMap()

    fun dialog(message: String, title: String, resumeGame: Boolean = false)

    fun displayFeedbackModal(feedback: Feedback, card: Card, target: Character? = null)

}