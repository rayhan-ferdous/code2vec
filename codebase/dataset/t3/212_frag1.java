    public void set(int nArgs, FtsAtom args[]) {

        removeAllPoints();

        if (nArgs > 0) {

            int j = 0;

            for (int i = 0; i < nArgs; i += 2) addPoint(j++, new BpfPoint((float) args[i].doubleValue, (float) args[i + 1].doubleValue));

            notifyPointAdded(j - 1);

        }

    }
