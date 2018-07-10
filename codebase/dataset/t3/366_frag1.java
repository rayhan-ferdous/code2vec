    URL getResourceAsURL(final ClassLoader cl, final String name) {

        return (URL) AccessController.doPrivileged(new PrivilegedAction() {



            public Object run() {

                URL url;

                if (cl == null) {

                    url = ClassLoader.getSystemResource(name);

                } else {

                    url = cl.getSystemResource(name);

                }

                return url;

            }

        });

    }
