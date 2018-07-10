        private Vector copyNuclei(Vector nuclei) {

            Vector newNuclei = new Vector();

            Enumeration e = nuclei.elements();

            Nucleus n = null;

            while (e.hasMoreElements()) {

                n = (Nucleus) e.nextElement();

                newNuclei.add(n.copy());

            }

            Collections.sort(newNuclei, n);

            return newNuclei;

        }
