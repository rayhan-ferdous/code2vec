    private void tridiagonalize() {

        eig = new double[dimension];

        e = new double[dimension];

        for (int j = 0; j < dimension; j++) {

            eig[j] = smallMatrix[dimension - 1][j];

        }

        for (int i = dimension - 1; i > 0; i--) {

            double scale = 0.0;

            double h = 0.0;

            for (int k = 0; k < i; k++) {

                scale = scale + Math.abs(eig[k]);

            }

            if (scale == 0.0) {

                e[i] = eig[i - 1];

                for (int j = 0; j < i; j++) {

                    eig[j] = smallMatrix[i - 1][j];

                    smallMatrix[i][j] = 0.0;

                    smallMatrix[j][i] = 0.0;

                }

            } else {

                for (int k = 0; k < i; k++) {

                    eig[k] /= scale;

                    h += eig[k] * eig[k];

                }

                double f = eig[i - 1];

                double g = Math.sqrt(h);

                if (f > 0) {

                    g = -g;

                }

                e[i] = scale * g;

                h = h - f * g;

                eig[i - 1] = f - g;

                for (int j = 0; j < i; j++) {

                    e[j] = 0.0;

                }

                for (int j = 0; j < i; j++) {

                    f = eig[j];

                    smallMatrix[j][i] = f;

                    g = e[j] + smallMatrix[j][j] * f;

                    for (int k = j + 1; k <= i - 1; k++) {

                        g += smallMatrix[k][j] * eig[k];

                        e[k] += smallMatrix[k][j] * f;

                    }

                    e[j] = g;

                }

                f = 0.0;

                for (int j = 0; j < i; j++) {

                    e[j] /= h;

                    f += e[j] * eig[j];

                }

                double hh = f / (h + h);

                for (int j = 0; j < i; j++) {

                    e[j] -= hh * eig[j];

                }

                for (int j = 0; j < i; j++) {

                    f = eig[j];

                    g = e[j];

                    for (int k = j; k <= i - 1; k++) {

                        smallMatrix[k][j] -= (f * e[k] + g * eig[k]);

                    }

                    eig[j] = smallMatrix[i - 1][j];

                    smallMatrix[i][j] = 0.0;

                }

            }

            eig[i] = h;

        }

        for (int i = 0; i < dimension - 1; i++) {

            smallMatrix[dimension - 1][i] = smallMatrix[i][i];

            smallMatrix[i][i] = 1.0;

            double h = eig[i + 1];

            if (h != 0.0) {

                for (int k = 0; k <= i; k++) {

                    eig[k] = smallMatrix[k][i + 1] / h;

                }

                for (int j = 0; j <= i; j++) {

                    double g = 0.0;

                    for (int k = 0; k <= i; k++) {

                        g += smallMatrix[k][i + 1] * smallMatrix[k][j];

                    }

                    for (int k = 0; k <= i; k++) {

                        smallMatrix[k][j] -= g * eig[k];

                    }

                }

            }

            for (int k = 0; k <= i; k++) {

                smallMatrix[k][i + 1] = 0.0;

            }

        }

        for (int j = 0; j < dimension; j++) {

            eig[j] = smallMatrix[dimension - 1][j];

            smallMatrix[dimension - 1][j] = 0.0;

        }

        smallMatrix[dimension - 1][dimension - 1] = 1.0;

        e[0] = 0.0;

    }
