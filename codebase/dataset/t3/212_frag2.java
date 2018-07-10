    public void append(int nArgs, FtsAtom args[]) {

        int j = length();

        if (nArgs > 1) {

            for (int i = 0; i < nArgs; i += 2) addPoint(j++, new BpfPoint((float) args[i].doubleValue, (float) args[i + 1].doubleValue));

            notifyPointAdded(j - 1);

        }

    }
