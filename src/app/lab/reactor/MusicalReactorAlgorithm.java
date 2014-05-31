package app.lab.reactor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import app.exceptions.lab.reactor.NoReactionException;
import app.lab.reactor.models.Atom;
import app.lab.reactor.models.Matter;
import app.lab.reactor.models.Molecule;
import app.lab.reactor.models.Molecule.MoleculeLayer;
import app.lab.rules.AtomAtomCollisionRule;
import app.lab.rules.CollisionRule;
import app.lab.rules.MoleculeAtomCollisionRule;
import app.lab.rules.MoleculeMoleculeCollisionRule;

/**
 * Part of the Tank, for basic version.
 * Responsible with applying the recombination rules <- from musical theory
 * @author Cristi
 */
public final class MusicalReactorAlgorithm extends ReactorAlgorithm {
	public final int TIMEOUT = 5000;
	
	protected MusicalReactorAlgorithm() {
		super();
	}
	
	public Collection<Matter> mixMatter(Collection<Matter> sample) throws NoReactionException {
		Collection<Matter> result = new ArrayList<Matter>();
		List<Matter> indexedSample = new ArrayList<Matter>(sample);
		
		Random random = new Random();
	
		
		
		for (int round = 0; round < TIMEOUT; round++) {
			if (indexedSample.size() < 2) {
				break;
			}
			
			int hit1 = random.nextInt(indexedSample.size());
			int hit2 = hit1;
			while (hit1 == hit2) {
				hit2 = random.nextInt(indexedSample.size());
			}
			
			Matter m1 = indexedSample.get(hit1);
			Matter m2 = indexedSample.get(hit2);
			
			if (hit1 > hit2) {
				indexedSample.remove(hit1);
				indexedSample.remove(hit2);
			} else {
				indexedSample.remove(hit2);
				indexedSample.remove(hit1);
			}
			
			
			if (m1 instanceof Atom && m2 instanceof Atom) {
				if (CollisionRule.getRule(AtomAtomCollisionRule.class).canApply(m1, m2, null)) {
					MoleculeLayer layer = MoleculeLayer.values()[random.nextInt(MoleculeLayer.values().length)];
					
					result.add(CollisionRule.getRule(AtomAtomCollisionRule.class).apply(m1, m2, layer));
				}
				continue;
			}
			
			if (m1 instanceof Atom && m2 instanceof Molecule || 
					m1 instanceof Molecule && m2 instanceof Atom) {
				Matter base, addition;
				
				if (m1 instanceof Molecule) {
					base = m1;
					addition = m2;
				} else {
					base = m2;
					addition = m1;
				}
				
				if (CollisionRule.getRule(MoleculeAtomCollisionRule.class).canApply(base, addition, null)) {
					MoleculeLayer layer = MoleculeLayer.values()[random.nextInt(MoleculeLayer.values().length)];
					
					result.add(CollisionRule.getRule(MoleculeAtomCollisionRule.class).apply(base, addition, layer));
				}
				continue;
			}
			
			if (m1 instanceof Molecule && m2 instanceof Molecule) {
				if (CollisionRule.getRule(MoleculeMoleculeCollisionRule.class).canApply(m1, m2, null)) {
					result.add(CollisionRule.getRule(MoleculeMoleculeCollisionRule.class).apply(m1, m2, null));
				}
				continue;
			}
		}
		
		return result;
	}

	@Override
	protected void initialize() {
		instance = this;
	}
}
