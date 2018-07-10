            if (j == nr_class) {

                if (nr_class == max_nr_class) {

                    max_nr_class *= 2;

                    int[] new_data = new int[max_nr_class];

                    System.arraycopy(label, 0, new_data, 0, label.length);

                    label = new_data;

                    new_data = new int[max_nr_class];

                    System.arraycopy(count, 0, new_data, 0, count.length);

                    count = new_data;

                }

                label[nr_class] = this_label;

                count[nr_class] = 1;

                ++nr_class;

            }

        }
