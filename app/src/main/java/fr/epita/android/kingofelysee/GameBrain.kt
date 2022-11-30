package fr.epita.android.kingofelysee

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import fr.epita.android.kingofelysee.objects.Character

class GameBrain : ViewModel()  {
    var characters = listOf<Character>()
    var partyStarted : Boolean = false
    var characterTurnIndex : Int = (0..5).random()
    var nbTurn : Int = 0
    var gamePaused : Boolean = false
    val hill = MutableLiveData<MutableSet<Character>>()

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

    fun getAllNonPlayerCharacters(): List<Character> {
        return this.characters.filter { c -> !c.isThePlayer_ }
    }

}