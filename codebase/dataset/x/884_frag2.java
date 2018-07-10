    public static Tuple<Float, Integer> maximum_and_location(Map_f map) {

        Tuple<Float, Integer> max = new Tuple<Float, Integer>(-1F, -1);

        float[] list = map.get_map();

        for (int u = 0; u < list.length; u++) {

            if (list[u] > max.get_first()) {

                max.set_first(list[u]);

                max.set_second(u);

            }

        }

        return max;

    }
