    public int get_lru_pos() {

        long[] my_last_used = last_used;

        long min_time = my_last_used[cache_size - 1];

        int low = cache_size - 1;

        int k;

        for (k = 0; k < cache_size; k++) {

            if (my_last_used[k] < min_time) {

                min_time = my_last_used[k];

                low = k;

            }

            ;

        }

        ;

        return low;

    }
