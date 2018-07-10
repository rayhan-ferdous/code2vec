        public URL getUrl() {

            try {

                return new URL(get(MovieProperty.url));

            } catch (Exception e) {

                return null;

            }

        }
