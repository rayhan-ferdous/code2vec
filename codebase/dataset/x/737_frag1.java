            String line = fp.readLine();

            StringTokenizer st = new StringTokenizer(line, " \t\n\r\f:");

            for (int k = 0; k < m; k++) model.sv_coef[k][i] = atof(st.nextToken());
