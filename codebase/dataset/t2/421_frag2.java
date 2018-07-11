                if ((k + 1 < m) & (e[k] != 0.0)) {

                    for (int i = k + 1; i < m; i++) {

                        work[i] = 0.0;

                    }

                    for (int j = k + 1; j < n; j++) {

                        for (int i = k + 1; i < m; i++) {

                            work[i] += e[j] * A[i][j];

                        }

                    }

                    for (int j = k + 1; j < n; j++) {

                        double t = -e[j] / e[k + 1];

                        for (int i = k + 1; i < m; i++) {

                            A[i][j] += t * work[i];

                        }

                    }

                }

                if (wantv) {

                    for (int i = k + 1; i < n; i++) {

                        V[i][k] = e[i];

                    }

                }

            }
