                } catch (IllegalArgumentException iare) {

                    browser = null;

                    errorMessage = iare.getMessage();

                    return browser;

                } catch (IllegalAccessException iae) {

                    browser = null;

                    errorMessage = iae.getMessage();

                    return browser;

                } catch (InvocationTargetException ite) {
