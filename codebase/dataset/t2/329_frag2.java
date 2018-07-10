                for (int i = 0; i < header.samples; ++i) {

                    C[i] = dis.readUnsignedByte();

                    if (C[i] > max) max = C[i];

                }

                for (int i = 0; i < header.samples; ++i) {

                    G[i] = dis.readUnsignedByte();

                    if (G[i] > max) max = G[i];

                }

                for (int i = 0; i < header.samples; ++i) {

                    T[i] = dis.readUnsignedByte();

                    if (T[i] > max) max = T[i];

                }

            }
