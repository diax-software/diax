package me.diax.diax.commands.fun;

import me.diax.comportment.jdacommand.Command;
import me.diax.comportment.jdacommand.CommandAttribute;
import me.diax.comportment.jdacommand.CommandDescription;
import me.diax.diax.util.Emote;
import net.dv8tion.jda.core.entities.Message;

import java.util.Random;

@CommandDescription(
        name = "die",
        triggers = {
                "die", "dice", "roll"
        },
        description = "{number/dice notation}",
        attributes = {
                @CommandAttribute(key = "private"),
        }
)
public class Die implements Command {

    private Random random;
    private static final int MAX_ROLLS = 50000;
    private static final int MAX_SIDES = 50000;

    public Die() {
        random = new Random();
    }

    @Override
    public void execute(Message message, String s) {
        long result;
        if (s.isEmpty()) {
            result = random.nextInt(6) + 1;
        } else {
            try {
                result = parseDiceRoll(s);
            } catch (Exception e) {
                message.getChannel().sendMessage(Emote.X + " - Invalid dice roll!\nExamples: \n```" +
                        "Examples:\n" +
                        "<>roll 1\n" +
                        "<>roll 2d10\n" +
                        "<>roll 1d4+20\n" +
                        "<>roll 1d5-2```"
                ).queue();
                return;
            }
        }
        message.getChannel().sendMessage(Emote.GAME_DIE + " - You rolled a `" + result + "`").queue();
    }

    /**
     * Parses a dice roll like <code><b>6,8d22+5,1d5</b></code>
     *
     * @param roll The dice roll that is being parsed
     * @return The complete result of the dice roll(s)
     * @throws Exception, used to catch any {@link IndexOutOfBoundsException} or any other Exception that may occur
     */
    public long parseDiceRoll(String roll) throws Exception {
        roll = roll.replace(" ", "");
        if (roll.contains(",")) {
            String[] parts = roll.split(",");
            long finalResult = 0;
            for (int i = 0; i < parts.length; i++)
                finalResult += resultDieceRoll(parts[i]);
            return finalResult;
        } else return resultDieceRoll(roll);
    }

    /**
     * Results a dice roll like <code><b>5d11</b></code> or <code><b>7d17-1</b></code>
     *
     * @param adb The dice roll to parse and result
     * @return The complete result of the dice roll
     * @throws Exception, used to catch any {@link IndexOutOfBoundsException} or any other Exception that may occur
     */
    private long resultDieceRoll(String adb) throws Exception {
        long a = 0, b = 0, result = 0;
        if (!adb.contains("d")) {
            long[] change = getDiceChange(adb);
            try {
                if (change.length > 0) b = change[0];
                else b = Long.parseLong(adb);
            } catch (NumberFormatException nfe) {
                throw new IllegalArgumentException("The input must be a Number");
            }
            result += (random.nextInt((int)b) + 1 + (change.length > 0 ? change[1] : 0));
            return result;
        } else {
            String[] parts = adb.split("d");
            long[] change = getDiceChange(parts[1]);
            try {
                a = Long.parseLong(parts[0]);
                if (change.length > 0) b = change[0];
                else b = Long.parseLong(parts[1]);
            } catch (NumberFormatException nfe) {
                throw new IllegalArgumentException("Not a valid format, A or B is supposed to be a Number");
            }
            if (a < 1 || b < 1) throw new IllegalArgumentException("Not a valid format, A or B cannot be Negative");
            if (a > MAX_ROLLS || b > MAX_SIDES || a > Integer.MAX_VALUE || b > Integer.MAX_VALUE)
                throw new IllegalArgumentException("Numbers are too Big, you are not rolling Dice here anymore.");
            for (int i = 0; i < a; i++)
                result += (random.nextInt((int)b) + 1 + (change.length > 0 ? change[1] : 0));
            return result;
        }
    }

    /**
     * This handles dice throws that look like <b><code>2d7-5</code></b> or <b><code>8d10+6</code></b>
     *
     * @param b The string to be tested
     * @return A int array of the parts in case a <b><code>+</code></b> or <b><code>-</code></b> was given
     * @throws Exception, used to catch any {@link IndexOutOfBoundsException} or any other Exception that may occur
     */
    private long[] getDiceChange(String b) throws Exception {
        long[] parts = new long[2];
        if (b.contains("+")) {
            String[] sparts = b.split("\\+");
            try {
                parts[0] = Long.parseLong(sparts[0]);
                parts[1] = Long.parseLong(sparts[1]);
            } catch (NumberFormatException nfe) {
                parts = new long[0];
            }
            return parts;
        } else if (b.contains("-")) {
            String[] sparts = b.split("-");
            try {
                parts[0] = Long.parseLong(sparts[0]);
                parts[1] = -Long.parseLong(sparts[1]);
            } catch (NumberFormatException nfe) {
                parts = new long[0];
            }
            return parts;
        } else return new long[0];
    }
}