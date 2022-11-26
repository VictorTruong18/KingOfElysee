package fr.epita.android.kingofelysee

import androidx.lifecycle.ViewModel
import fr.epita.android.kingofelysee.objects.Character

class GameBrain : ViewModel()  {
    lateinit var character : Character

    fun chooseCharacter(character : Character){
        this.character = character
    }
}