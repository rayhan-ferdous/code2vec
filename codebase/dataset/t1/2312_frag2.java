            if (isChange) {

                lo++;

            } else {

                int T = fSortOrder[start_hi];

                for (int k = start_hi - 1; k >= lo; k--) {

                    fSortOrder[k + 1] = fSortOrder[k];

                }

                fSortOrder[lo] = T;

                lo++;

                end_lo++;

                start_hi++;

            }

        }

    }
