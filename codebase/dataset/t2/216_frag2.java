    protected float[] readFloatArray(DataInputStream dis, int size) throws IOException {

        float[] data = new float[size];

        for (int i = 0; i < size; i++) {

            data[i] = readFloat(dis);

        }

        return data;

    }
