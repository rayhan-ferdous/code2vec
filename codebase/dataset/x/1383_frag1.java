    public static Set<HXPoint> getHexAround(HXPoint centre, int rayon) {

        if (rayon < 0) {

            throw new IllegalArgumentException("Rayon doit �tre >= 0");

        } else if (rayon == 0) {

            return Collections.singleton(centre);

        } else {

            HashSet<HXPoint> hexs = new HashSet<HXPoint>();

            HX3D centre3d = to3D(centre);

            for (int dx = -rayon; dx <= rayon; dx++) {

                int ymin = dx >= 0 ? -rayon : -rayon - dx;

                int ymax = dx >= 0 ? rayon - dx : rayon;

                for (int dy = ymin; dy <= ymax; dy++) {

                    int dz = dx + dy;

                    HXPoint p = to2D(centre3d.x + dx, centre3d.y + dy, centre3d.z + dz);

                    hexs.add(p);

                }

            }

            return hexs;

        }

    }
