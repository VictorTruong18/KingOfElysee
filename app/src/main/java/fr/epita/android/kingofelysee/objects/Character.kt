package fr.epita.android.kingofelysee.objects

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

val LIFE_POINTS = 10
val ENERGY_POINTS = 0

class Character(
    name: String,
    image: Int
) : ViewModel(){
    val name_ : String = name
    val image_ : Int = image

    var isThePlayer_ : Boolean = false
    var lifePoints_ = MutableLiveData<Int>(LIFE_POINTS)
    var energyPoints_ = MutableLiveData<Int>(ENERGY_POINTS)
    var onTheHill_ : Boolean = false
    var isMyTurn_ : Boolean = false
    var isAlive_ : Boolean = true

    fun incrementLifePoints(lifepoints : Int){
        this.lifePoints_.postValue(this.lifePoints_.value!!.plus(lifepoints))
    }

    fun lifePointsString(): String {
        return this.lifePoints_.value?.toString() ?: "error"
    }

    fun incrementEnergyPoints(energypoints : Int){
        this.energyPoints_.postValue(this.energyPoints_.value!!.plus(energypoints))
    }

    fun energyPointsString(): String {
        return this.energyPoints_.value?.toString() ?: "error"
    }

}