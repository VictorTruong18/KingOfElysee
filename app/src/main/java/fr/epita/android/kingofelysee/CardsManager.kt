package fr.epita.android.kingofelysee

import fr.epita.android.kingofelysee.objects.Card
import fr.epita.android.kingofelysee.objects.Effect
import fr.epita.android.kingofelysee.objects.Character
import kotlin.random.Random

class CardsManager(val characters: List<Character>) {
    private val cards = setOf (
        Card(R.drawable.bad_coke, Effect.IMMEDIATE, true),
        Card(R.drawable.bogoss, Effect.DELAYED, false),
        Card(R.drawable.bollore, Effect.IMMEDIATE, false),
        Card(R.drawable.casseroles, Effect.DELAYED, false),
        Card(R.drawable.cauchemar, Effect.IMMEDIATE, true),
        Card(R.drawable.cgt, Effect.DELAYED, false),
        Card(R.drawable.chinois, Effect.IMMEDIATE, false),
        Card(R.drawable.coke, Effect.IMMEDIATE, false),
        Card(R.drawable.don, Effect.DELAYED, true),
        Card(R.drawable.hannounah, Effect.IMMEDIATE, false),
        Card(R.drawable.inflation, Effect.IMMEDIATE, false),
        Card(R.drawable.interview_bfm, Effect.IMMEDIATE, false),
        Card(R.drawable.masque, Effect.IMMEDIATE, false),
        Card(R.drawable.mcfly, Effect.IMMEDIATE, false),
        Card(R.drawable.mckinsey, Effect.DELAYED, false),
        Card(R.drawable.perlimpimpin, Effect.IMMEDIATE, true),
        Card(R.drawable.pieces, Effect.IMMEDIATE, false),
        Card(R.drawable.vacances, Effect.IMMEDIATE, false),
        Card(R.drawable.valls, Effect.IMMEDIATE, true),
    )

    fun useCard(card: Card, user: Character, target: Character? = null) : Boolean {
        if (card.hasToChooseTarget && target == null) return false

        // From now on we know that if we need a target its not going to be null
        when (card.id) {
            R.drawable.bad_coke -> return badCokeEffect(target!!)
            R.drawable.bogoss -> return bogossEffect(user)
            R.drawable.bollore -> return bolloreEffect(user)
            R.drawable.casseroles -> return casserolesEffect(user, target!!)
            R.drawable.cauchemar -> return cauchemarEffect(target!!)
            R.drawable.cgt -> return cgtEffect(user, target!!)
            R.drawable.chinois -> return chinoisEffect(user)
            R.drawable.coke -> return cokeEffect(user)
            R.drawable.don -> return donEffect(user, target!!)
            R.drawable.hannounah -> return hannounahEffect(user)
            R.drawable.inflation -> return inflationEffect(user)
            R.drawable.interview_bfm -> return interviewBFMEffect(user)
            R.drawable.masque -> return masqueEffect(user)
            R.drawable.mcfly -> return mcflyEffect(user)
            R.drawable.mckinsey -> return mckinseyEffect(user)
            R.drawable.perlimpimpin -> return perlimpinpinEffect(user, target!!)
            R.drawable.pieces -> return pieceEffect(user)
            R.drawable.vacances -> return vacancesEffect(user)
            R.drawable.valls -> return vallsEffect(target!!)
        }
        return false
    }

    fun getCard(card: Card) : Card? {
        return cards.find { it.id == card.id }
    }

    fun getRandomCard() : Card {
        return cards.elementAt(Random.nextInt(cards.size))
    }

    private fun badCokeEffect(target: Character) : Boolean {
        target.incrementLifePoints(-2)
        return true
    }

    private fun bogossEffect(user: Character) : Boolean {
        characters.forEach {
            if (it == user) return@forEach

            it.incrementLifePoints(-1)
        }

        user.incrementVictoryPoints(3)

        return true
    }

    private fun bolloreEffect(user: Character) : Boolean {
        characters.forEach {
            if (it == user) return@forEach

            it.incrementEnergyPoints(-1)
            it.incrementLifePoints(-1)
        }
        return true
    }

    private fun casserolesEffect(user: Character, target: Character) : Boolean {
        if (user.victoryPoints_.value!! > target.victoryPoints_.value!!) {
            target.incrementVictoryPoints(-2)
            return true
        }
        return false
    }

    private fun cauchemarEffect(target: Character) : Boolean {
        target.incrementVictoryPoints(-1)
        return true
    }

    private fun cgtEffect(user: Character, target: Character) : Boolean {
        if (target.energyPoints_.value!! > user.energyPoints_.value!!) {
            target.incrementEnergyPoints(-2)
            user.incrementEnergyPoints(2)
            return true
        }
        return false
    }

    private fun chinoisEffect(user: Character) : Boolean {
        user.incrementEnergyPoints(5)
        return true
    }

    private fun cokeEffect(user: Character) : Boolean {
        user.incrementLifePoints(3)
        return true
    }

    private fun donEffect(user: Character, target: Character) : Boolean {
        if (user.energyPoints_.value!! < 3) return false

        user.incrementVictoryPoints(1)
        user.incrementEnergyPoints(-3)

        target.incrementEnergyPoints(3)
        target.incrementVictoryPoints(-1)

        return true
    }

    private fun hannounahEffect(user: Character) : Boolean {
        user.incrementLifePoints(2)
        user.incrementVictoryPoints(1)

        return true
    }

    private fun inflationEffect(user: Character) : Boolean {
        characters.forEach {
            if (it == user) return@forEach

            it.incrementEnergyPoints(-3)
        }

        return true
    }

    private fun interviewBFMEffect(user: Character) : Boolean {
        user.incrementVictoryPoints(2)
        return true
    }

    private fun masqueEffect(user: Character) : Boolean {
        user.incrementLifePoints(2)
        user.incrementVictoryPoints(-2)
        return true
    }

    private fun mcflyEffect(user: Character) : Boolean {
        user.incrementVictoryPoints(2)
        return true
    }

    private fun mckinseyEffect(user: Character) : Boolean {
        if (user.energyPoints_.value!! < 3) return false

        user.incrementEnergyPoints(-3)
        user.incrementVictoryPoints(1)
        return true
    }

    private fun perlimpinpinEffect(user: Character, target: Character) : Boolean {
        user.incrementLifePoints(1)
        target.incrementVictoryPoints(-1)
        return true
    }

    private fun pieceEffect(user: Character) : Boolean {
        user.incrementEnergyPoints(3)
        return true
    }

    private fun vacancesEffect(user: Character) : Boolean {
        user.incrementLifePoints(2)
        user.incrementVictoryPoints(-1)
        return true
    }

    private fun vallsEffect(target: Character) : Boolean {
        target.incrementVictoryPoints(-1)
        return true
    }
}