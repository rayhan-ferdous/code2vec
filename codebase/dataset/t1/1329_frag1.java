    public Instances getDataSet() throws IOException {

        Instances result;

        double[] sparse;

        double[] data;

        int i;

        if (m_sourceReader == null) throw new IOException("No source has been specified");

        if (getRetrieval() == INCREMENTAL) throw new IOException("Cannot mix getting Instances in both incremental and batch modes");

        setRetrieval(BATCH);

        if (m_structure == null) getStructure();

        result = new Instances(m_structure, 0);

        for (i = 0; i < m_Buffer.size(); i++) {

            sparse = (double[]) m_Buffer.get(i);

            if (sparse.length != m_structure.numAttributes()) {

                data = new double[m_structure.numAttributes()];

                System.arraycopy(sparse, 0, data, 0, sparse.length - 1);

                data[data.length - 1] = sparse[sparse.length - 1];

            } else {

                data = sparse;

            }

            if (result.classAttribute().isNominal()) {

                if (data[data.length - 1] == 1.0) data[data.length - 1] = result.classAttribute().indexOfValue("+1"); else if (data[data.length - 1] == -1) data[data.length - 1] = result.classAttribute().indexOfValue("-1"); else throw new IllegalStateException("Class is not binary!");

            }

            result.add(new SparseInstance(1, data));

        }

        try {

            m_sourceReader.close();

        } catch (Exception ex) {

        }

        return result;

    }
