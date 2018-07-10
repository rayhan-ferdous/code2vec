        } catch (IllegalArgumentException iae2) {

            CustomLogger.INSTANCE.log(Level.WARNING, header + "illegal argument exception for class " + className + " - " + iae2.getMessage(), iae2);

            throw iae2;

        } catch (InvocationTargetException ite) {

            CustomLogger.INSTANCE.log(Level.WARNING, header + "invocation target exception for class " + className + " - " + ite.getMessage(), ite);

            throw ite;

        } catch (ClassCastException cce) {
