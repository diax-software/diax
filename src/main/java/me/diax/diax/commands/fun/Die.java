package me.diax.diax.commands.fun;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import me.diax.comportment.jdacommand.Command;
import me.diax.comportment.jdacommand.CommandAttribute;
import me.diax.comportment.jdacommand.CommandDescription;
import me.diax.diax.util.Emote;
import net.dv8tion.jda.core.entities.Message;

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

    public Die() {
        random = new Random();
    }

    @Override
    public void execute(Message message, String s) {
        int result;
        if (s.isEmpty()) {
            result = random.nextInt(6) + 1;
        } else {
            try {
                result = parseDice(s);
            } catch (IllegalArgumentException e) {
                message.getChannel().sendMessage(Emote.X + " - Invalid dice roll!").queue();
                return;
            }
        }
        message.getChannel().sendMessage(Emote.GAME_DIE + " - You rolled a `" + result + "`").queue();
    }
    
	public int parseDice(String in) {
		in = in.replace(" ", "");
		if(in.contains(",")) {
			String[] parts = in.split(",");
			int finalResult = 0;
			for(int i = 0; i < parts.length; i++) {
				List<Integer> parsed = resultDieceRoll(parts[i]);
				finalResult += parsed.stream().mapToInt(n -> n).sum();
			}
			return finalResult;
		} else {
			List<Integer> parsed = resultDieceRoll(in);
			return parsed.stream().mapToInt(n -> n).sum();
		}
	}
	
	private final int MAX_ROLLS = 50000;
	private final int MAX_SIDES = 50000;
	
	private List<Integer> resultDieceRoll(String adb) {
		List<Integer> list = new ArrayList<>();
		int a, b;
		if(!adb.contains("d")) {
			int change = getDiceChange(adb);
			try {
				if(change > 0) a = Integer.parseInt(adb.split("\\+")[0]);
				else if(change < 0) a = Integer.parseInt(adb.split("-")[0]);
				else a = Integer.parseInt(adb);
			} catch(NumberFormatException nfe) {
				throw new IllegalArgumentException("The input must be a Number");
			}
			list.add(random.nextInt(a) + 1 + change);
			return list;
		} else {
			String[] parts = adb.split("d");
			int change = getDiceChange(parts[1]);
			try {
				a = Integer.parseInt(parts[0]);
				if(change > 0) b = Integer.parseInt(parts[1].split("\\+")[0]);
				else if(change < 0) b = Integer.parseInt(parts[1].split("-")[0]);
				else b = Integer.parseInt(parts[1]);
			} catch(NumberFormatException nfe) {
				throw new IllegalArgumentException("Not a valid format, A or B is supposed to be a Number");
			}
			if(a < 1 || b < 1) throw new IllegalArgumentException("Not a valid format, A or B cannot be Negative");
			if(a > MAX_ROLLS || b > MAX_SIDES) throw new IllegalArgumentException("Numbers are too Big, you are not rolling Dice here anymore.");
			for(int i = 0; i < a; i++)
				list.add(random.nextInt(b) + 1 + change);
			return list;
		}
	}
	
	private int getDiceChange(String b) {
		int a;
		if(b.contains("+")) {
			try {
				a = Integer.parseInt(b.split("\\+")[1]);
			} catch(NumberFormatException nfe) {
				a = 0;
			}
			return a;
		} else if(b.contains("-")) {
			try {
				a = Integer.parseInt(b.split("-")[1]);
			} catch(NumberFormatException nfe) {
				a = 0;
			}
			return -a;
		} else return 0;
	}
    
}