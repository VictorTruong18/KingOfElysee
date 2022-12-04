package fr.epita.android.kingofelysee

import android.util.Log
import fr.epita.android.kingofelysee.objects.Card
import fr.epita.android.kingofelysee.objects.Effect
import fr.epita.android.kingofelysee.objects.Character
import kotlin.random.Random

class CardsManager() {
    private val cards = setOf(
        Card(R.drawable.bad_coke,
            Effect.IMMEDIATE,
            true,
            "%TARGET% perd deux points de vie",
            2
        ),
        Card(
            R.drawable.bogoss,
            Effect.DELAYED,
            false,
            "Bravo, vous avez gagné trois votes et fait perdre un point de vie à vos adversaires !",
            3
        ),
        Card(
            R.drawable.bollore,
            Effect.IMMEDIATE,
            false,
            "Bravo, vous avez fait perdre un point de vie et un vote à vos adversaires !",
            3
        ),
        Card(
            R.drawable.casseroles,
            Effect.DELAYED,
            true,
            "Bravo, %TARGET% avait plus de votes que vous, plus maintenant!",
            0 // TODO CHANGE ME
        ),
        Card(R.drawable.cauchemar, Effect.IMMEDIATE, true, "%TARGET% perd un vote"),
        Card(
            R.drawable.cgt,
            Effect.DELAYED,
            true,
            "Bravo, grâce à la CGT, vous ne vous faites plus de soucis !",
            0
        ),
        Card(
            R.drawable.chinois,
            Effect.IMMEDIATE,
            false,
            "Bravo, vous gagnez cinq sous grâce à la Chine !",
            0
        ),
        Card(R.drawable.coke, Effect.IMMEDIATE, false, "Bravo, vous avez fait le plein d'énergie"),
        Card(
            R.drawable.don,
            Effect.DELAYED,
            true,
            "Vous faites un arrangement avec %TARGET% et gagnez un de ses votes.",
            1
        ),
        Card(
            R.drawable.hannounah,
            Effect.IMMEDIATE,
            false,
            "Bravo, vous avez gagné un vote et un point de vie.",
            2
        ),
        Card(
            R.drawable.inflation,
            Effect.IMMEDIATE,
            false,
            "Bravo, tous vos adversaires perdent trois sous !",
            2
        ),
        Card(
            R.drawable.interview_bfm,
            Effect.IMMEDIATE,
            false,
            "Bravo, vous avez gagné deux votes !",
            2
        ),
        Card(
            R.drawable.masque,
            Effect.IMMEDIATE,
            false,
            "Bravo, vous gagnez deux points de vie mais perdez deux votes !",
            0
        ),
        Card(R.drawable.mcfly, Effect.IMMEDIATE,
            false,
            "Bravo, vous avez gagné 2 votes !",
            2
        ),
        Card(
            R.drawable.mckinsey,
            Effect.DELAYED,
            false,
            "Vous avez gagné un vote grâce à l'expertise de McKinsey.",
            0
        ),
        Card(
            R.drawable.perlimpimpin,
            Effect.IMMEDIATE,
            true,
            "%TARGET% perd un vote et vous gagnez un point de vie.",
            1
        ),
        Card(R.drawable.pieces,
            Effect.IMMEDIATE,
            false,
            "Bravo, vous gagnez trois sous !",
            0),
        Card(
            R.drawable.vacances,
            Effect.IMMEDIATE,
            false,
            "Vous gagnez deux points deux vie mais perdez un vote.",
            0
        ),
        Card(R.drawable.valls,
            Effect.IMMEDIATE,
            true,
            "%TARGET% perd un vote.",
            1),
    )

    fun useCard(
        card: Card,
        user: Character,
        target: Character? = null,
        characters: List<Character>
    ): Feedback {
        if (card.hasToChooseTarget && target == null) return Feedback.HAS_TO_CHOOSE_TARGET

        // From now on we know that if we need a target its not going to be null
        when (card.id) {
            R.drawable.bad_coke -> return badCokeEffect(target!!)
            R.drawable.bogoss -> return bogossEffect(user, characters)
            R.drawable.bollore -> return bolloreEffect(user, characters)
            R.drawable.casseroles -> return casserolesEffect(user, target!!)
            R.drawable.cauchemar -> return cauchemarEffect(target!!)
            R.drawable.cgt -> return cgtEffect(user, target!!)
            R.drawable.chinois -> return chinoisEffect(user)
            R.drawable.coke -> return cokeEffect(user)
            R.drawable.don -> return donEffect(user, target!!)
            R.drawable.hannounah -> return hannounahEffect(user)
            R.drawable.inflation -> return inflationEffect(user, characters)
            R.drawable.interview_bfm -> return interviewBFMEffect(user)
            R.drawable.masque -> return masqueEffect(user)
            R.drawable.mcfly -> return mcflyEffect(user)
            R.drawable.mckinsey -> return mckinseyEffect(user)
            R.drawable.perlimpimpin -> return perlimpinpinEffect(user, target!!)
            R.drawable.pieces -> return pieceEffect(user)
            R.drawable.vacances -> return vacancesEffect(user)
            R.drawable.valls -> return vallsEffect(target!!)
        }
        throw java.lang.IllegalStateException("It is not theoretically possible to have an id that is not known")
    }

    fun getCard(card: Card): Card? {
        return cards.find { it.id == card.id }
    }

    fun getRandomCard(): Card {
        return cards.elementAt(Random.nextInt(cards.size))
    }

    private fun badCokeEffect(target: Character): Feedback {
        target.incrementLifePoints(-2)
        return Feedback.VALID
    }

    private fun bogossEffect(user: Character, characters: List<Character>): Feedback {
        characters.forEach {
            if (it == user) return@forEach

            it.incrementLifePoints(1)
        }

        user.incrementVictoryPoints(3)

        return Feedback.VALID
    }

    private fun bolloreEffect(user: Character, characters: List<Character>): Feedback {
        characters.forEach {
            if (it == user) return@forEach

            it.incrementEnergyPoints(-1)
            it.incrementLifePoints(-1)
        }
        return Feedback.VALID
    }

    private fun casserolesEffect(user: Character, target: Character): Feedback {
        if (target.victoryPoints_.value!! > user.victoryPoints_.value!!) {
            target.incrementVictoryPoints(-2)
            return Feedback.VALID
        }
        return Feedback.TARGET_NOT_ENOUGH_VP
    }

    private fun cauchemarEffect(target: Character): Feedback {
        target.incrementVictoryPoints(-1)
        return Feedback.VALID
    }

    private fun cgtEffect(user: Character, target: Character): Feedback {
        if (target.energyPoints_.value!! > user.energyPoints_.value!! + 2) {
            Log.d("Lounes", "Target is: ${target.name_}, Energy: ${target.energyPoints_.value}")
            target.incrementEnergyPoints(-2)
            user.incrementEnergyPoints(2)
            return Feedback.VALID
        }
        return Feedback.TARGET_NOT_ENOUGH_ENERGY
    }

    private fun chinoisEffect(user: Character): Feedback {
        user.incrementEnergyPoints(5)
        return Feedback.VALID
    }

    private fun cokeEffect(user: Character): Feedback {
        user.incrementLifePoints(3)
        return Feedback.VALID
    }

    private fun donEffect(user: Character, target: Character): Feedback {
        if (user.energyPoints_.value!! < 3) return Feedback.USER_NOT_ENOUGH_ENERGY

        user.incrementVictoryPoints(1)
        user.incrementEnergyPoints(-3)

        target.incrementEnergyPoints(3)
        target.incrementVictoryPoints(-1)

        return Feedback.VALID
    }

    private fun hannounahEffect(user: Character): Feedback {
        user.incrementLifePoints(2)
        user.incrementVictoryPoints(1)

        return Feedback.VALID
    }

    private fun inflationEffect(user: Character, characters: List<Character>): Feedback {
        characters.forEach {
            if (it == user) return@forEach

            it.incrementEnergyPoints(-3)
        }

        return Feedback.VALID
    }

    private fun interviewBFMEffect(user: Character): Feedback {
        user.incrementVictoryPoints(2)
        return Feedback.VALID
    }

    private fun masqueEffect(user: Character): Feedback {
        if (user.victoryPoints_.value!! < 2) return Feedback.USER_NOT_ENOUGH_VP
        user.incrementLifePoints(2)
        user.incrementVictoryPoints(-2)
        return Feedback.VALID
    }

    private fun mcflyEffect(user: Character): Feedback {
        user.incrementVictoryPoints(2)
        return Feedback.VALID
    }

    private fun mckinseyEffect(user: Character): Feedback {
        if (user.energyPoints_.value!! < 3) return Feedback.USER_NOT_ENOUGH_ENERGY

        user.incrementEnergyPoints(-3)
        user.incrementVictoryPoints(1)
        return Feedback.VALID
    }

    private fun perlimpinpinEffect(user: Character, target: Character): Feedback {
        user.incrementLifePoints(1)
        target.incrementVictoryPoints(-1)
        return Feedback.VALID
    }

    private fun pieceEffect(user: Character): Feedback {
        user.incrementEnergyPoints(3)
        return Feedback.VALID
    }

    private fun vacancesEffect(user: Character): Feedback {
        user.incrementLifePoints(2)
        user.incrementVictoryPoints(-1)
        return Feedback.VALID
    }

    private fun vallsEffect(target: Character): Feedback {
        target.incrementVictoryPoints(-1)
        return Feedback.VALID
    }
}

enum class Feedback {
    VALID,
    HAS_TO_CHOOSE_TARGET,
    TARGET_NOT_ENOUGH_ENERGY,
    TARGET_NOT_ENOUGH_VP,
    USER_NOT_ENOUGH_HP,
    USER_NOT_ENOUGH_ENERGY,
    USER_NOT_ENOUGH_VP
}