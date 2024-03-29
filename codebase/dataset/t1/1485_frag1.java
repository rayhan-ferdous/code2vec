            }

            boolean[] nonzero = new boolean[l];

            for (i = 0; i < l; i++) nonzero[i] = false;

            decision_function[] f = new decision_function[nr_class * (nr_class - 1) / 2];

            double[] probA = null, probB = null;

            if (param.probability == 1) {

                probA = new double[nr_class * (nr_class - 1) / 2];

                probB = new double[nr_class * (nr_class - 1) / 2];

            }

            int p = 0;

            for (i = 0; i < nr_class; i++) for (int j = i + 1; j < nr_class; j++) {

                svm_problem sub_prob = new svm_problem();

                int si = start[i], sj = start[j];

                int ci = count[i], cj = count[j];

                sub_prob.l = ci + cj;

                sub_prob.x = new svm_node[sub_prob.l][];

                sub_prob.y = new double[sub_prob.l];

                int k;

                for (k = 0; k < ci; k++) {

                    sub_prob.x[k] = x[si + k];

                    sub_prob.y[k] = +1;

                }

                for (k = 0; k < cj; k++) {

                    sub_prob.x[ci + k] = x[sj + k];

                    sub_prob.y[ci + k] = -1;

                }

                if (param.probability == 1) {

                    double[] probAB = new double[2];

                    svm_binary_svc_probability(sub_prob, param, weighted_C[i], weighted_C[j], probAB);

                    probA[p] = probAB[0];

                    probB[p] = probAB[1];

                }

                f[p] = svm_train_one(sub_prob, param, weighted_C[i], weighted_C[j]);

                for (k = 0; k < ci; k++) if (!nonzero[si + k] && Math.abs(f[p].alpha[k]) > 0) nonzero[si + k] = true;

                for (k = 0; k < cj; k++) if (!nonzero[sj + k] && Math.abs(f[p].alpha[ci + k]) > 0) nonzero[sj + k] = true;

                ++p;

            }

            model.nr_class = nr_class;

            model.label = new int[nr_class];

            for (i = 0; i < nr_class; i++) model.label[i] = label[i];

            model.rho = new double[nr_class * (nr_class - 1) / 2];

            for (i = 0; i < nr_class * (nr_class - 1) / 2; i++) model.rho[i] = f[i].rho;

            if (param.probability == 1) {

                model.probA = new double[nr_class * (nr_class - 1) / 2];

                model.probB = new double[nr_class * (nr_class - 1) / 2];

                for (i = 0; i < nr_class * (nr_class - 1) / 2; i++) {

                    model.probA[i] = probA[i];

                    model.probB[i] = probB[i];

                }

            } else {

                model.probA = null;

                model.probB = null;

            }

            int nnz = 0;

            int[] nz_count = new int[nr_class];

            model.nSV = new int[nr_class];

            for (i = 0; i < nr_class; i++) {

                int nSV = 0;

                for (int j = 0; j < count[i]; j++) if (nonzero[start[i] + j]) {
