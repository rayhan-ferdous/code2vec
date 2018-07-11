                        int sx = in.readShort();

                        int sy = in.readShort();

                        cb.moveTo(state.transformX(sx), state.transformY(sy));

                        for (int k = 1; k < len; ++k) {

                            int x = in.readShort();
