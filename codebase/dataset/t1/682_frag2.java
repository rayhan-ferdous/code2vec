                if (strElement.charAt(1) == 't') {

                    if (isotope == 0) return (massAccuracy == MONOISOTOPIC) ? 209.987131 : 0.000000;

                    if (isotope == 210) return 209.987131;

                    break;

                }

                if (strElement.charAt(1) == 'u') {

                    if (isotope == 0) return (massAccuracy == MONOISOTOPIC) ? 196.966552 : 196.966552;

                    if (isotope == 197) return 196.966552;

                    break;

                }

                break;
