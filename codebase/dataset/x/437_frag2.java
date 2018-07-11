    public static <T extends ZipNode> T load(ZipInputStream zip_in, ZipEntry entry, Class<T> type, ZipStreamFilter filter) {

        T ret = null;

        try {

            ByteArrayInputStream bais = new ByteArrayInputStream(ZipUtil.readBytes(zip_in));

            ret = type.cast(filter.readNode(bais, type));

        } catch (Throwable ex) {

            ex.printStackTrace();

        }

        return ret;

    }
