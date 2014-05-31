package app.lab.rules;

import app.lab.reactor.models.Atom;
import app.lab.reactor.models.Matter;
import app.lab.reactor.models.Molecule;
import app.lab.reactor.models.Molecule.MoleculeLayer;
import app.music.PitchedSound;
import app.music.theory.HorizontalCoherenceMusicalRule;
import app.music.theory.MusicalRule;

public class AtomAtomCollisionRule extends CollisionRule {

	@Override
	public boolean canApply(Matter base, Matter addition, MoleculeLayer targetLayer) {
		PitchedSound from = ((Atom) base).getInfo();
		PitchedSound to = ((Atom) addition).getInfo();
		
		return MusicalRule.getRule(HorizontalCoherenceMusicalRule.class).intervalComplies(from, to);
	}

	@Override
	public Matter apply(Matter base, Matter addition, MoleculeLayer targetLayer) {
		Atom baseAtom = (Atom) base;
		Atom additionAtom = (Atom) addition;
		Molecule molecule = null;
		
		switch (targetLayer) {
			case Bottom:
				molecule = new Molecule();
				molecule.addAtom(baseAtom, MoleculeLayer.Top);
				molecule.addAtom(additionAtom, MoleculeLayer.Bottom);
				break;
			case Top:
				molecule = new Molecule();
				molecule.addAtom(baseAtom, MoleculeLayer.Top);
				molecule.addAtom(additionAtom, MoleculeLayer.Top);
				break;
		}
		
		return molecule;
	}

}
