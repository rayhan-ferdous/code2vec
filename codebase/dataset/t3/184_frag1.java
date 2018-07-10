    private static void solve_epsilon_svr(svm_problem prob, svm_parameter param, double[] alpha, Solver.SolutionInfo si) {

        int l = prob.l;

        double[] alpha2 = new double[2 * l];

        double[] linear_term = new double[2 * l];

        byte[] y = new byte[2 * l];

        int i;

        for (i = 0; i < l; i++) {

            alpha2[i] = 0;

            linear_term[i] = param.p - prob.y[i];

            y[i] = 1;

            alpha2[i + l] = 0;

            linear_term[i + l] = param.p + prob.y[i];

            y[i + l] = -1;

        }

        Solver s = new Solver();

        s.Solve(2 * l, new SVR_Q(prob, param), linear_term, y, alpha2, param.C, param.C, param.eps, si, param.shrinking);

        double sum_alpha = 0;

        for (i = 0; i < l; i++) {

            alpha[i] = alpha2[i] - alpha2[i + l];

            sum_alpha += Math.abs(alpha[i]);

        }

        System.out.print("nu = " + sum_alpha / (param.C * l) + "\n");

    }
