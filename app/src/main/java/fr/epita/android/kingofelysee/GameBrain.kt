package fr.epita.android.kingofelysee

import androidx.lifecycle.ViewModel

class GameBrain : ViewModel()  {
    var chosenCharacter : String = ""

    fun chooseCharacter(characterName : String){
        this.chosenCharacter = characterName
    }
}