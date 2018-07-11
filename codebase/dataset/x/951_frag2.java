    void processCoordinates() throws Exception {

        readLines(3);

        int expectedAtomNumber = 0;

        if (!chargesFound) {

            atomSetCollection.newAtomSet();

            baseAtomIndex = atomSetCollection.getAtomCount();

        } else {

            chargesFound = false;

        }

        Atom[] atoms = atomSetCollection.getAtoms();

        while (readLine() != null) {

            int atomNumber = parseInt(line);

            if (atomNumber == Integer.MIN_VALUE) break;

            ++expectedAtomNumber;

            if (atomNumber != expectedAtomNumber) throw new Exception("unexpected atom number in coordinates");

            String elementSymbol = parseToken();

            Atom atom = atoms[baseAtomIndex + atomNumber - 1];

            if (atom == null) {

                atom = atomSetCollection.addNewAtom();

            }

            atom.atomSerial = atomNumber;

            setAtomCoord(atom, parseFloat(), parseFloat(), parseFloat());

            int atno = parseInt(elementSymbol);

            if (atno != Integer.MIN_VALUE) elementSymbol = getElementSymbol(atno);

            atom.elementSymbol = elementSymbol;

        }

    }
