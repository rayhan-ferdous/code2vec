                } catch (ClassNotFoundException cnfe) {

                    errorMessage = cnfe.getMessage();

                    return false;

                } catch (NoSuchFieldException nsfe) {

                    errorMessage = nsfe.getMessage();

                    return false;

                } catch (NoSuchMethodException nsme) {

                    errorMessage = nsme.getMessage();

                    return false;

                } catch (SecurityException se) {
