                                if (bottom[gx] >= 0) walk[bottom[gx]][1] = trilen;

                                if (gx > 0 && broken[curgrid][gx] < gy) {

                                    walk[trilen][2] = tlr[qmg - 1][2];

                                    walk[tlr[qmg - 1][2]][0] = trilen;

                                } else walk[trilen][2] = -1;

                                walk[trilen][1] = trilen + 1;
