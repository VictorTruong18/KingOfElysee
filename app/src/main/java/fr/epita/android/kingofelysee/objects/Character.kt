package fr.epita.android.kingofelysee.objects

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

val LIFE_POINTS = 10
val ENERGY_POINTS = 0
val VICTORY_POINTS = 0

class Character(
    name: String,
    image: Int
) : ViewModel(){
    val name_ : String = name
    val image_ : Int = image

    var fragment_id_ : Int = 0
    var isThePlayer_ : Boolean = false
    var lifePoints_ = MutableLiveData<Int>(LIFE_POINTS)
    var energyPoints_ = MutableLiveData<Int>(ENERGY_POINTS)
    var victoryPoints_ = MutableLiveData<Int>(VICTORY_POINTS)
    var onTheHill_ : Boolean = false
    var isMyTurn_ : Boolean = false

    var cards = MutableLiveData(mutableListOf<Card>())

    fun addCard(card: Card) {
        this.cards.value!!.add(card)
        cards.value = cards.value
    }

    fun removeCard(card: Card) {
        this.cards.value!!.remove(card)
        cards.value = cards.value
    }

    fun incrementLifePoints(lifepoints : Int){
        this.lifePoints_.postValue(this.lifePoints_.value!!.plus(lifepoints))
    }

    fun incrementEnergyPoints(energypoints : Int){
        this.energyPoints_.postValue(this.energyPoints_.value!!.plus(energypoints))
    }

    fun incrementVictoryPoints(victorypoints : Int){
        this.victoryPoints_.postValue(this.victoryPoints_.value!!.plus(victorypoints))
    }

}