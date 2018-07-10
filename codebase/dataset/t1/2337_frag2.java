    private static Set getOpcodes(Method[] emitters) {

        Set s = new HashSet();

        for (int i = 0; i < emitters.length; i++) {

            String name = emitters[i].getName();

            if (DEBUG) System.out.println(name);

            if (name.startsWith("emit")) {

                int posOf_ = name.indexOf('_');

                if (posOf_ != -1) {

                    String opcode = name.substring(4, posOf_);

                    if (!excludedOpcodes.contains(opcode)) s.add(opcode);

                } else {

                    String opcode = name.substring(4);

                    if (opcode.equals(opcode.toUpperCase(Locale.getDefault()))) if (!excludedOpcodes.contains(opcode)) s.add(opcode);

                }

            }

        }

        return s;

    }
