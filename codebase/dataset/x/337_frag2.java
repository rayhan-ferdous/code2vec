                } catch (IllegalAccessException iae) {

                    browser = null;

                    errorMessage = iae.getMessage();

                    return browser;

                } catch (InstantiationException ie) {

                    browser = null;

                    errorMessage = ie.getMessage();

                    return browser;

                } catch (InvocationTargetException ite) {
