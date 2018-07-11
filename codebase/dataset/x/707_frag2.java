            medians = getMedians(clusters);

            variances = getVariances(clusters, medians);

        }

        float[] tempConv = new float[clusterConvergence.length];

        for (int i = 0; i < clusterConvergence.length; i++) {
