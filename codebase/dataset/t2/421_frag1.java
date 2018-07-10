                if (((k + 1) < m) && (e[k] != 0.0f)) {

                    for (int i = k + 1; i < m; i++) {

                        work[i] = 0.0f;

                    }

                    for (int j = k + 1; j < n; j++) {

                        for (int i = k + 1; i < m; i++) {

                            work[i] += e[j] * mat[i][j];

                        }

                    }

                    for (int j = k + 1; j < n; j++) {

                        final float t = -e[j] / e[k + 1];

                        for (int i = k + 1; i < m; i++) {

                            mat[i][j] += t * work[i];

                        }

                    }

                }

                if (wantv) {

                    for (int i = k + 1; i < n; i++) {

                        v[i][k] = e[i];

                    }

                }

            }
