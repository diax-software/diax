package me.diax.diax.commands.action;

import me.diax.comportment.jdacommand.Command;

import java.util.Random;

/**
 * This class is used to represent an action-command in Diax.
 * An action command consists of a target user being interacted with,
 * an image will always be used to represent this interaction.
 *
 * @author comportment
 * @since 0.0.1
 */
public interface ActionCommand extends Command {

    /**
     * A way to get all of the images used in this action command.
     *
     * @return A String[] containing all the images to be used.
     * @author comportment
     * @since 0.0.1
     */
    String[] getImages();

    /**
     * A way to get a random image from the ones specified in {@link #getImages()}
     *
     * @return A String containing the URL of an image to be used.
     * @author comportment
     * @since 0.0.1
     */
    default String getImage() {
        String[] imgs = getImages();
        return "https://media.giphy.com/media/" + imgs[new Random().nextInt(imgs.length)] + "/giphy.gif";
    }
}
