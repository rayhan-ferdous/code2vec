    public static String predictAlignedSecondaryStructure(String original) {

        String sequence = CStringHelper.replace(original, "-", "");

        String structure = predictSecondaryStructure(sequence);

        StringBuilder buffer = new StringBuilder();

        int position = 0;

        for (int index = 0; index < original.length(); index++) {

            if (position >= structure.length()) {

                buffer.append("-");

                continue;

            }

            char aa = original.charAt(index);

            if (aa == '-') buffer.append('-'); else {

                buffer.append(structure.charAt(position));

                position++;

            }

        }

        String aligned = buffer.toString();

        return aligned;

    }
