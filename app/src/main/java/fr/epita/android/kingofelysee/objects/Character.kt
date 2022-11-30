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

    fun incrementLifePoints(lifepoints : Int){
        var lf = lifepoints
        if(this.lifePoints_.value!! + lifepoints > 10)
            lf = 10 - this.lifePoints_.value!!
        this.lifePoints_.postValue(this.lifePoints_.value!!.plus(lf))
    }

    fun incrementEnergyPoints(energypoints : Int){
        this.energyPoints_.postValue(this.energyPoints_.value!!.plus(energypoints))
    }

    fun incrementVictoryPoints(victorypoints : Int){
        var vt = victorypoints
        if(this.victoryPoints_.value!! + victorypoints > 20)
            vt = 20 - this.victoryPoints_.value!!
        this.victoryPoints_.postValue(this.victoryPoints_.value!!.plus(vt))
    }

}