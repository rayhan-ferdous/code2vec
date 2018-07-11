    protected SenoneSequence getSenoneSequence(int[] stateid) {

        Senone[] senones = new Senone[stateid.length];

        for (int i = 0; i < stateid.length; i++) {

            senones[i] = (Senone) senonePool.get(stateid[i]);

        }

        return new SenoneSequence(senones);

    }
