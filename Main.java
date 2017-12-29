// Final Game Project (Rocket Game)
// By John Ajala and Gina Girgis
// 29 November, 2017
/* This program is a game of a rocket that shoots rocks with red bullets. It has an animated background and
 * assimilates the space where the rocket leaves trying to fight.
 * The main is a driver class that calls the renderer and sets the frame to visible */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Main {

	public static void main(String[] args) { 
		// Create the Game Frame
		JFrame frame = new JFrame("Rocket Game"); //create the frame
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //exit command
		
		Renderer render = new Renderer(); //instantiate the renderer class
		
		// Disable resize to keep image background intact
		frame.setResizable(false);
		// Set the content pane
		frame.getContentPane().add(render);

	    frame.pack();
	    frame.setVisible(true);
	}

}
