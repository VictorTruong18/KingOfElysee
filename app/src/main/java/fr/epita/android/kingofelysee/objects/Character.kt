package fr.epita.android.kingofelysee.objects

val LIFE_POINTS = 10
val ENERGY_POINTS = 0

class Character(
    name: String,
    image: Int,
    isThePlayer: Boolean
) {
    val name_ : String = name
    val image_ : Int = image
    val isThePlayer_ : Boolean = isThePlayer

    var lifePoints_ : Int = LIFE_POINTS
    var energyPoints_ : Int = ENERGY_POINTS
    var onTheHill_ : Boolean = false
    var isMyTurn_ : Boolean = false
    var isAlive_ : Boolean = true

}