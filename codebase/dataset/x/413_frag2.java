                putIntoCache(name, o);

            } catch (IllegalArgumentException ex) {

                ex.printStackTrace();

            } catch (SecurityException ex) {

                ex.printStackTrace();

            } catch (NoSuchMethodException ex) {

                ex.printStackTrace();

            } catch (IllegalAccessException ex) {

                ex.printStackTrace();

            } catch (InvocationTargetException ex) {

                ex.printStackTrace();
