package me.diax.diax.commands.`fun`

import me.diax.comportment.jdacommand.Command
import me.diax.comportment.jdacommand.CommandAttribute
import me.diax.comportment.jdacommand.CommandDescription
import me.diax.diax.util.Emote
import net.dv8tion.jda.core.entities.Message
import java.util.*

@CommandDescription(name = "die", triggers = arrayOf("die", "dice", "roll"), description = "{number/dice notation}", attributes = arrayOf(CommandAttribute(key = "private")))
class Die : Command {

    private val random: Random = Random()

    override fun execute(message: Message, s: String) {
        val result: Long
        if (s.isEmpty()) {
            result = (random.nextInt(6) + 1).toLong()
        } else {
            try {
                result = parseDiceRoll(s)
            } catch (e: Exception) {
                message.channel.sendMessage(Emote.X + " - Invalid dice roll!\nExamples: \n```" +
                        "Examples:\n" +
                        "<>roll 1\n" +
                        "<>roll 2d10\n" +
                        "<>roll 1d4+20\n" +
                        "<>roll 1d5-2```"
                ).queue()
                return
            }

        }
        message.channel.sendMessage(Emote.GAME_DIE + " - You rolled a `" + result + "`").queue()
    }

    /**
     * Parses a dice roll like `**6,8d22+5,1d5**`
     *
     * @param roll The dice roll that is being parsed
     * @return The complete result of the dice roll(s)
     * @throws Exception, used to catch any [IndexOutOfBoundsException] or any other Exception that may occur
     */
    @Throws(Exception::class)
    fun parseDiceRoll(roll: String): Long {
        var rol = roll
        rol = rol.replace(" ", "")
        if (rol.contains(",")) {
            val parts = rol.split(",".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
            var finalResult: Long = 0
            for (i in parts.indices)
                finalResult += resultDiceRoll(parts[i])
            return finalResult
        } else
            return resultDiceRoll(rol)
    }

    /**
     * Results a dice roll like `**5d11**` or `**7d17-1**`
     *
     * @param adb The dice roll to parse and result
     * @return The complete result of the dice roll
     * @throws Exception, used to catch any [IndexOutOfBoundsException] or any other Exception that may occur
     */
    @Throws(Exception::class)
    private fun resultDiceRoll(adb: String): Long {
        var a: Long = 0
        var b: Long = 0
        var result: Long = 0
        if (!adb.contains("d")) {
            val change = getDiceChange(adb)
            try {
                when (change.isNotEmpty()) {
                    true -> b = change[0]
                    false -> b = adb.toLong()
                }
            } catch (nfe: NumberFormatException) {
                throw IllegalArgumentException("The input must be a Number")
            }

            result += random.nextInt(b.toInt()).toLong() + 1 + if (change.size > 0) change[1] else 0
            return result
        } else {
            val parts = adb.split("d".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
            val change = getDiceChange(parts[1])
            try {
                a = parts[0].toLong()
                when (change.isNotEmpty()) {
                    true -> b = change[0]
                    false -> b = parts[1].toLong()
                }
            } catch (nfe: NumberFormatException) {
                throw IllegalArgumentException("Not a valid format, A or B is supposed to be a Number")
            }

            if (a < 1 || b < 1) throw IllegalArgumentException("Not a valid format, A or B cannot be Negative")
            if (a > MAX_ROLLS || b > MAX_SIDES || a > Integer.MAX_VALUE || b > Integer.MAX_VALUE)
                throw IllegalArgumentException("Numbers are too Big, you are not rolling Dice here anymore.")
            for (i in 0..a - 1)
                result += random.nextInt(b.toInt()).toLong() + 1 + if (change.isNotEmpty()) change[1] else 0
            return result
        }
    }

    /**
     * This handles dice throws that look like **`2d7-5`** or **`8d10+6`**
     *
     * @param b The string to be tested
     * @return A int array of the parts in case a **`+`** or **`-`** was given
     * @throws Exception, used to catch any [IndexOutOfBoundsException] or any other Exception that may occur
     */
    @Throws(Exception::class)
    private fun getDiceChange(b: String): LongArray {
        var parts = LongArray(2)
        if (b.contains("+")) {
            val sparts = b.split("\\+".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
            try {
                parts[0] = sparts[0].toLong()
                parts[1] = sparts[1].toLong()
            } catch (nfe: NumberFormatException) {
                parts = LongArray(0)
            }

            return parts
        } else if (b.contains("-")) {
            val sparts = b.split("-".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
            try {
                parts[0] = sparts[0].toLong()
                parts[1] = -sparts[1].toLong()
            } catch (nfe: NumberFormatException) {
                parts = LongArray(0)
            }

            return parts
        } else
            return LongArray(0)
    }

    companion object {
        private val MAX_ROLLS = 50000
        private val MAX_SIDES = 50000
    }
}