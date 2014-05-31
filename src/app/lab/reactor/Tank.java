package app.lab.reactor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import app.exceptions.lab.reactor.NoReactionException;
import app.exceptions.lab.reactor.ReactorAlgorithmSetupException;
import app.lab.reactor.models.Atom;
import app.lab.reactor.models.Matter;
import app.lab.reactor.models.Molecule;
import app.music.Note;
import app.music.Octave;
import app.music.PitchedSound;

/**
 * Here is where the atoms = notes live
 * Current version: just keep track of how many there are
 * Probable later version: more complex simulation - trajectory, collisions, energy, etc.
 * @author Cristi
 *
 */

public class Tank implements Runnable {
	private Collection<Matter> matter;
	private Random random;
	
	public Tank(Class<? extends ReactorAlgorithm> algorithmType) throws ReactorAlgorithmSetupException {
		matter = new ArrayList<Matter>();
		random = new Random(System.currentTimeMillis());
		
		ReactorAlgorithmFactory.setupReactorAlgorithm(algorithmType);
	}
	
	public void addMatter(Collection<Matter> matter) {
		this.matter.addAll(matter);
	}
	
	public void addTestMatter() {
		
		for (Note note : Note.values()) {
			int copies = 4;
			
			Octave octaves[] = {Octave.Third, Octave.Fourth};
			
			for (Octave oct : octaves) {
				for (int i = 0; i < copies; i++) {
//					if (note.equals(Note.F) || note.equals(Note.C)) {
//						matter.add(new Atom(PitchedSound.create(note, oct)));
//						matter.add(new Atom(PitchedSound.create(note, oct, Intonation.Sharp)));
//						continue;
//					}
//					
//					if (note.equals(Note.B) || note.equals(Note.E)) {
//						matter.add(new Atom(PitchedSound.create(note, oct)));
//						matter.add(new Atom(PitchedSound.create(note, oct, Intonation.Flat)));
//						continue;
//					}
//						
//					for (Intonation into : Intonation.values()) {
//						matter.add(new Atom(PitchedSound.create(note, oct, into)));
//					}
					
					matter.add(new Atom(PitchedSound.create(note, oct)));
				
					
					
				}
			}
		}
	}
	
	@Override
	public void run() {
		
		while (matter.size() > 1) {
			
			Collection<Matter> sample = new ArrayList<Matter>();
			int nr = 2 + random.nextInt(matter.size() / 2);
			List<Boolean> hits = new ArrayList<Boolean>();
			
			for (int i = 0; i < nr; i++) {
				hits.add(true);
			}
			
			for (int i = nr; i < matter.size(); i++) {
				hits.add(false);
			}
			
			Collections.shuffle(hits);
			
			Iterator<Matter> matterIt = matter.iterator();
			Iterator<Boolean> hitsIt = hits.iterator();
			
			while (matterIt.hasNext()) {
				if (hitsIt.next()) {
					sample.add(matterIt.next());
				} else {
					matterIt.next();
				}
			}
			
			Iterator<Matter> sampleIt = sample.iterator();
			
			while (sampleIt.hasNext()) {
				matter.remove(sampleIt.next());
			}
			
			try {
				matter.addAll(ReactorAlgorithm.getInstance().mixMatter(sample));
			} catch (NoReactionException e) {
				;
			}
		}
		
		synchronized (this) {
			this.notify();
		}
	}
	
	public Collection<Matter> getMatter() {
		return matter;
	}
	
	public Molecule getLargestMolecule() {
		int maxSize = 0;
		Molecule largestMolecle = null;
		
		for (Matter m : matter) {
			if (m instanceof Molecule) {
				Molecule molecule = (Molecule) m;
				if (molecule.getAtoms().size() > maxSize) {
					maxSize = molecule.getAtoms().size();
					largestMolecle = molecule;
				}
			}
		}
		
		return largestMolecle;
	}
}
