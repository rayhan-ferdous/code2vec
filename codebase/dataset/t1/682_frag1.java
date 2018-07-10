                if (strElement.charAt(1) == 's') {

                    if (isotope == 0) return (massAccuracy == MONOISOTOPIC) ? 74.921596 : 74.921596;

                    if (isotope == 75) return 74.921596;

                    break;

                }

                if (strElement.charAt(1) == 't') {

                    if (isotope == 0) return (massAccuracy == MONOISOTOPIC) ? 209.987131 : 0.000000;

                    if (isotope == 210) return 209.987131;

                    break;

                }

                if (strElement.charAt(1) == 'u') {
