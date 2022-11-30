package fr.epita.android.kingofelysee

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import fr.epita.android.kingofelysee.objects.Card
import fr.epita.android.kingofelysee.objects.Character
import java.util.*
import kotlin.random.Random.Default.nextInt

class GameBrain : ViewModel()  {
    var characters = listOf<Character>()
    var partyStarted : Boolean = false
    var characterTurnIndex : Int = (0..5).random()
    var nbTurn : Int = 0
    var gamePaused : Boolean = false
    val hill = MutableLiveData<MutableSet<Character>>()

    val cardsManager = CardsManager(characters)
    private var shopCards = Pair(cardsManager.getRandomCard(), cardsManager.getRandomCard())

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

    fun getShopCards() : Pair<Card, Card> {
        return shopCards
    }

    fun renewShopCards() {
        this.shopCards = Pair(cardsManager.getRandomCard(), cardsManager.getRandomCard())
    }

    fun buyCard(cardNbr: Int) {
        val currentPlayer = characters[characterTurnIndex]
        // TODO CHECK PLAYER MONEY AND DEDUCT IT
        val card = if (cardNbr == 0) shopCards.first else shopCards.second

        currentPlayer.cards.add(card)

        if (cardNbr == 0) {
            this.shopCards = Pair(cardsManager.getRandomCard(), this.shopCards.second)
        } else {
            this.shopCards = Pair(this.shopCards.first, cardsManager.getRandomCard())
        }
    }
}