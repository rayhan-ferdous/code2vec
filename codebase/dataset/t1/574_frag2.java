        Pool pool = new Pool(path);

        ExtendedStreamTokenizer est = new ExtendedStreamTokenizer(inputStream, '#', false);

        est.expectString("mixw");

        numStates = est.getInt("numStates");

        numStreams = est.getInt("numStreams");

        numGaussiansPerState = est.getInt("numGaussiansPerState");
