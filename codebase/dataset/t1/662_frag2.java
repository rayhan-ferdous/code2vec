            subprob.x = new svm_node[subprob.l][];

            subprob.y = new double[subprob.l];

            k = 0;

            for (j = 0; j < begin; j++) {

                subprob.x[k] = prob.x[perm[j]];

                subprob.y[k] = prob.y[perm[j]];

                ++k;

            }

            for (j = end; j < prob.l; j++) {

                subprob.x[k] = prob.x[perm[j]];

                subprob.y[k] = prob.y[perm[j]];

                ++k;

            }

            int p_count = 0, n_count = 0;

            for (j = 0; j < k; j++) if (subprob.y[j] > 0) p_count++; else n_count++;
