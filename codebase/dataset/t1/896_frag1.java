    protected void parseURL(URL arg0, String arg1, int arg2, int arg3) {

        URLStreamHandler handler = factory.findAuthorizedURLStreamHandler(protocol);

        if (handler != null) {

            try {

                handlerField.set(arg0, handler);

                parseURLMethod.invoke(handler, new Object[] { arg0, arg1, new Integer(arg2), new Integer(arg3) });

                handlerField.set(arg0, this);

                return;

            } catch (Exception e) {

                factory.adaptor.getFrameworkLog().log(new FrameworkLogEntry(MultiplexingURLStreamHandler.class.getName(), "parseURL", FrameworkLogEntry.ERROR, e, null));

                throw new RuntimeException(e.getMessage());

            }

        }

        throw new IllegalStateException();

    }
