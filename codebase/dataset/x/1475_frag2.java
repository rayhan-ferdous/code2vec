    protected void startContext() throws Exception {

        super.doStart();

        if (_errorHandler != null) _errorHandler.start();

        if (_contextListeners != null) {

            ServletContextEvent event = new ServletContextEvent(_scontext);

            for (int i = 0; i < LazyList.size(_contextListeners); i++) {

                ((ServletContextListener) LazyList.get(_contextListeners, i)).contextInitialized(event);

            }

        }

    }
