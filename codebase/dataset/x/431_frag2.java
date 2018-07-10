    public static AnnotationManager instance() {

        if (null == instance) {

            instance = new AnnotationManager();

        }

        return instance;

    }
