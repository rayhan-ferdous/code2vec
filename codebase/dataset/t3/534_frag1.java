            } else if (cmd.startsWith("degree")) param.degree = atof(arg); else if (cmd.startsWith("gamma")) param.gamma = atof(arg); else if (cmd.startsWith("coef0")) param.coef0 = atof(arg); else if (cmd.startsWith("nr_class")) model.nr_class = atoi(arg); else if (cmd.startsWith("total_sv")) model.l = atoi(arg); else if (cmd.startsWith("rho")) {

                int n = model.nr_class * (model.nr_class - 1) / 2;

                model.rho = new double[n];

                StringTokenizer st = new StringTokenizer(arg);

                for (int i = 0; i < n; i++) model.rho[i] = atof(st.nextToken());

            } else if (cmd.startsWith("label")) {

                int n = model.nr_class;

                model.label = new int[n];

                StringTokenizer st = new StringTokenizer(arg);

                for (int i = 0; i < n; i++) model.label[i] = atoi(st.nextToken());

            } else if (cmd.startsWith("probA")) {
