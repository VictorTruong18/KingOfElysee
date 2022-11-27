package fr.epita.android.kingofelysee

import androidx.lifecycle.ViewModel
import fr.epita.android.kingofelysee.objects.Character

class GameBrain : ViewModel()  {
    lateinit var player : Character
    var characters = listOf<Character>()

    fun chooseCharacter(character : Character){
        this.player = character
    }

    fun initAllCharacters(character : Array<Character>){
        this.characters = character.toList()
    }
}