package app;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import org.jfugue.MusicStringParser;
import org.jfugue.MusicXmlRenderer;
import org.jfugue.Pattern;
import org.jfugue.Player;

import app.exceptions.lab.reactor.ReactorAlgorithmSetupException;
import app.lab.reactor.MusicalReactorAlgorithm;
import app.lab.reactor.Tank;
import app.lab.reactor.models.Molecule;
import app.music.MusicalPhrase;
import app.music.PitchedSound;

public class Test {

	public static void writeMusicXML(String filename, Pattern song) {
		MusicXmlRenderer renderer = new MusicXmlRenderer();
		MusicStringParser parser = new MusicStringParser();
		parser.addParserListener(renderer);
		parser.parse(song);
		
		try {
			File outFile = new File(filename);
			PrintWriter writer;
			writer = new PrintWriter(outFile);
			writer.println(renderer.getMusicXMLString());
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		
//		song.add("V0 C4 F4 G4 R C5 " + 
//		         "V1 E3 R F3 G3 C4");
//		player.play(song);
//		
//		

		
		
		Tank tank;
		try {
			tank = new Tank(MusicalReactorAlgorithm.class);
			tank.addTestMatter();
			
			new Thread(tank).start();
			
			synchronized (tank) {
				try {
					tank.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			Molecule m = (Molecule) tank.getLargestMolecule();
			
			if (m == null) {
				System.out.println("No molecule created... weird!");
			} else {
				MusicalPhrase phrase = MusicalPhrase.fromMatter(m);
				
				System.out.println(phrase);
				
				StringBuilder strBld = new StringBuilder();
				
				strBld.append("V0 ");
						
				for (PitchedSound sound : phrase.getVoice(0)) {
					strBld.append(sound.toMusicString()).append(" ");
				}
				
				strBld.append("V1 ");
				
				for (PitchedSound sound : phrase.getVoice(1)) {
					strBld.append(sound.toMusicString()).append(" ");
				}
				
				Pattern song = new Pattern(strBld.toString());
				Player player = new Player();
				player.play(song);
				
				writeMusicXML("out.music.xml", song);
			}
			
			
		} catch (ReactorAlgorithmSetupException e1) {
			
			e1.printStackTrace();
		}
		
		

	}

}
