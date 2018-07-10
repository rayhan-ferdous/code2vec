    public static void serializeTo(File file, Serializable s) {

        try {

            FileOutputStream fos = new FileOutputStream(file);

            ObjectOutputStream oos = new ObjectOutputStream(fos);

            oos.writeObject(s);

            oos.close();

        } catch (IOException e) {

            throw new RuntimeException(e);

        }

    }
