package fr.epita.android.kingofelysee

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import fr.epita.android.kingofelysee.objects.Card
import fr.epita.android.kingofelysee.objects.Character

class GameBrain : ViewModel()  {
    var characters = listOf<Character>()
    var partyStarted : Boolean = false
    var characterTurnIndex : Int = (0..5).random()
    var nbTurn : Int = 0
    var gamePaused : Boolean = false
    val hill = MutableLiveData<MutableSet<Character>>()

    private val cardsManager = CardsManager()
    var shopCards = MutableLiveData(Pair(cardsManager.getRandomCard(), cardsManager.getRandomCard()))

    fun initAllCharacters(character : Array<Character>){
        this.characters = character.toList()
        hill.value = mutableSetOf()
    }

    fun addToHill(character: Character){
        hill.value?.add(character)
        hill.value = hill.value
    }

    fun removeFromHill(character: Character){
        hill.value?.remove(character)
        hill.value = hill.value
    }

    fun getCurrentPlayer() : Character {
        return characters[characterTurnIndex]
    }

    fun getPlayersWithoutCurrentPlayer() : List<Character> {
        return characters.filter { it != getCurrentPlayer() }
    }

    fun renewShopCards() {
        this.shopCards.value = Pair(cardsManager.getRandomCard(), cardsManager.getRandomCard())
    }

    fun buyCard(cardNbr: Int) {
        val currentPlayer = characters[characterTurnIndex]
        // TODO CHECK PLAYER MONEY AND DEDUCT IT
        val card = if (cardNbr == 0) shopCards.value!!.first else shopCards.value!!.second

        // TODO FEEDBACK
        if (currentPlayer.cards.size >= 6) return
        currentPlayer.cards.add(card)

        if (cardNbr == 0) {
            this.shopCards.value = Pair(cardsManager.getRandomCard(), this.shopCards.value!!.second)
        } else {
            this.shopCards.value = Pair(this.shopCards.value!!.first, cardsManager.getRandomCard())
        }
    }

    fun useShopCard(cardNbr: Int, user: Character, target: Character? = null) : Boolean {
        val card = if (cardNbr == 0) shopCards.value!!.first else shopCards.value!!.second

        if (cardNbr == 0) {
            Log.d("Lounes", "Removed card 0")
            this.shopCards.value = Pair(cardsManager.getRandomCard(), this.shopCards.value!!.second)
        } else {
            Log.d("Lounes", "Removed card 1")
            this.shopCards.value = Pair(this.shopCards.value!!.first, cardsManager.getRandomCard())
        }

        Log.d("Lounes", "In useShopCard")

        return cardsManager.useCard(card, user, target, this.characters)
    }

    fun useCard(card: Card, user: Character, target: Character? = null) : Boolean {
        return cardsManager.useCard(card, user, target, this.characters)
    }
}