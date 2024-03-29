    public static DBDData resolveDBD(DBDData data, String fileName) {

        if (data == null) data = new DBDData();

        EnhancedStreamTokenizer tokenizer = getEnhancedStreamTokenizer(fileName);

        if (tokenizer != null) {

            try {

                File file = new File(fileName);

                PathSpecification paths = new PathSpecification(file.getParentFile().getAbsolutePath());

                processDBD(data, tokenizer, fileName, paths);

            } catch (Exception e) {

                data = null;

            } finally {

                System.gc();

            }

        } else return null;

        return data;

    }
