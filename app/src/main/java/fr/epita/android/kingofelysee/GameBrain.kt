package fr.epita.android.kingofelysee

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import fr.epita.android.kingofelysee.objects.Card
import fr.epita.android.kingofelysee.objects.Character


class GameBrain : ViewModel() {
    var characters = listOf<Character>()
    var partyStarted: Boolean = false
    var characterTurnIndex: Int = (0..5).random()
    var nbTurn: Int = 0
    var hillTurn: Int = 0
    var gamePaused: Boolean = false
    val waitNext = MutableLiveData(false)
    val hill = MutableLiveData<MutableSet<Character>>()

    private val cardsManager = CardsManager()
    var shopCards = MutableLiveData(Pair(cardsManager.getRandomCard(), cardsManager.getRandomCard()))

    fun initAllCharacters(character : Array<Character>){
        this.characters = character.toList()
        hill.value = mutableSetOf()
    }

    fun addToHill(character: Character) {
        if(nbTurn > 0 && hillTurn != nbTurn) {
            character.incrementVictoryPoints(1)
            hillTurn = nbTurn
        }
        character.onTheHill_ = true
        hill.value?.add(character)
        hill.value = hill.value
    }

    fun removeFromHill(character: Character) {
        character.onTheHill_ = false
        hill.value?.remove(character)
        hill.value = hill.value
    }

    fun getCurrentPlayer() : Character {
        return characters[characterTurnIndex]
    }

    fun getPlayersWithoutCurrentPlayer() : List<Character> {
        return characters.filter { it != getCurrentPlayer() }
    }

    fun renewShopCards(renewPrice: Int) {
        val currentPlayer = this.getCurrentPlayer()
        currentPlayer.incrementEnergyPoints(renewPrice * -1)
        this.shopCards.value = Pair(cardsManager.getRandomCard(), cardsManager.getRandomCard())
    }

    fun buyCard(cardNbr: Int) : Boolean {
        val currentPlayer = this.getCurrentPlayer()

        val card = if (cardNbr == 0) shopCards.value!!.first else shopCards.value!!.second

        currentPlayer.incrementEnergyPoints(card.price * -1)

        if (currentPlayer.cards.value!!.size >= 6) return false

        currentPlayer.addCard(card)

        if (cardNbr == 0) {
            this.shopCards.value = Pair(cardsManager.getRandomCard(), this.shopCards.value!!.second)
        } else {
            this.shopCards.value = Pair(this.shopCards.value!!.first, cardsManager.getRandomCard())
        }
        return true
    }

    fun useShopCard(cardNbr: Int, target: Character? = null) : Feedback {
        val currentPlayer = this.getCurrentPlayer()

        val card = if (cardNbr == 0) shopCards.value!!.first else shopCards.value!!.second

        if (currentPlayer.energyPoints_.value!! < card.price)
            return Feedback.USER_NOT_ENOUGH_ENERGY

        currentPlayer.incrementEnergyPoints(card.price * -1)

        val feedback = cardsManager.useCard(card, currentPlayer, target, this.characters)

        if (feedback == Feedback.VALID) {
            if (cardNbr == 0) {
                this.shopCards.value =
                    Pair(cardsManager.getRandomCard(), this.shopCards.value!!.second)
            } else {
                this.shopCards.value =
                    Pair(this.shopCards.value!!.first, cardsManager.getRandomCard())
            }
        }
        return feedback
    }

    fun useCard(card: Card, target: Character? = null) : Feedback {
        val player = this.getCurrentPlayer()
        val feedback = cardsManager.useCard(card, player, target, this.characters)

        if (feedback == Feedback.VALID) {
            Log.d("Lounes", "Used card")
            player.removeCard(card)
        }
        return feedback
    }

    fun hasCurrentPlayerEnoughMoneyToBuyCard(cardNbr: Int) : Boolean {
        val card = if (cardNbr == 0) shopCards.value!!.first else shopCards.value!!.second

        val player = characters[characterTurnIndex]

        return player.energyPoints_.value!! >= card.price
    }

    fun canCurrentPlayerRenew(renewPrice: Int) : Boolean {
        val player = characters[characterTurnIndex]

        return player.energyPoints_.value!! >= renewPrice
    }
    
    fun play(character: Character, dice: List<String>, communicator: Communicator) {
        character.lastPlayTurn_ = nbTurn
        gamePaused = true
        if (character.isThePlayer_) {
            communicator.loadMap()
        }
        val count_attack = dice.count { it == "❗" }
        var count_money = dice.count { it == "\uD83D\uDCB6" }
        var count_life = dice.count { it == "♥" }

        val attack_character = characters.filter { c -> c.onTheHill_ != character.onTheHill_ }
        val out_of_hill: MutableList<Character> = listOf<Character>().toMutableList()
        attack_character.forEach {
            it.incrementLifePoints(-count_attack)
            if (it.onTheHill_) {
                if (it.isThePlayer_) {
                    it.canResignTurn_ = nbTurn
                } else {
                    it.onTheHill_ = ((1..10).random() < it.lifePoints_.value!!)
                }
                if (!it.onTheHill_) {
                    removeFromHill(it)
                    out_of_hill.add(it)
                }
            }
        }
        count_money = character.incrementEnergyPoints(count_money)
        count_life = character.incrementLifePoints(if (character.onTheHill_) 0 else count_life)

        var count_vote = 0
        for (i in 1 .. 3){
            val count_current = dice.count { it == "$i" }
            if (count_current >= 3) {
                count_vote += i + (count_current - 3)
            }
        }
        count_vote = character.incrementVictoryPoints(count_vote)

        val lifeMessage = "$count_life ♥ récolté(s)"
        val attackMessage = "$count_attack ❗ " + (if (character.onTheHill_) "contre les opposants" else "contre le pouvoir")
        val donationMessage = "$count_money \uD83D\uDCB6 reçu(s)"
        val votesMessage = "$count_vote \uD83D\uDDF3️ récolté(s)"
        val outHillMessage = if(out_of_hill.size > 0) "Démission de " + out_of_hill.joinToString(separator = ", ") { it.name_ } else "Aucun politique n'a démissionné"

        communicator.dialog(
            lifeMessage + "\n" + donationMessage + "\n" + votesMessage + "\n" + attackMessage + "\n" + outHillMessage,
            character.name_ + (if (character.isThePlayer_) " (Vous)" else "")
        )

    }

    fun getAllNonPlayerCharacters(): List<Character> {
        return this.characters.filter { c -> !c.isThePlayer_ }
    }


    fun getHillCapacity(): Int {
        return if(characters.filter { it.lifePoints_.value!! > 0 }.size > 4) 2 else 1
    }



}