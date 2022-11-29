package fr.epita.android.kingofelysee

import androidx.lifecycle.ViewModel
import fr.epita.android.kingofelysee.objects.Character

class GameBrain : ViewModel()  {
    var characters = listOf<Character>()

    fun initAllCharacters(character : Array<Character>){
        this.characters = character.toList()
    }
}