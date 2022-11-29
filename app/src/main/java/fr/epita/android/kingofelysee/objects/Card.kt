package fr.epita.android.kingofelysee.objects

abstract class Card (val usable: Usable, val on: Effect, val characters: List<Character>, val drawableId: Int) {
    abstract fun use()
}

enum class Usable {
    IMMEDIATELY,
    DELAYED
}

enum class Effect {
    ON_ONE_ENEMY,
    ON_ME,
    ON_EVERYONE
}