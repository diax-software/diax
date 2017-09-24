package me.diax.diax.commands.action;

import me.diax.comportment.jdacommand.Command;

import java.util.Random;

public interface ActionCommand extends Command {

    String[] getImages();

    default String getImage() {
        String[] imgs = getImages();
        return imgs[new Random().nextInt(imgs.length)];
    }
}
