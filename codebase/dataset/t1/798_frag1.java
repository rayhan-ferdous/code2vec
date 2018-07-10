    private int sideSizeInBytes() {

        int numLevels = getNumMipMaps();

        if (numLevels == 0) {

            numLevels = 1;

        }

        int size = 0;

        for (int i = 0; i < numLevels; i++) {

            size += mipMapSizeInBytes(i);

        }

        return size;

    }
