    public Terminal getEmulation() throws IOException {

        if (emulation == null) {

            emulation = terminals.getTerminal(term);

        }

        return emulation;

    }
