    public Object callReadResolve(Object result) {

        if (result == null) {

            return null;

        } else {

            Method readResolveMethod = getMethod(result.getClass(), "readResolve", null, true);

            if (readResolveMethod != null) {

                try {

                    return readResolveMethod.invoke(result, EMPTY_ARGS);

                } catch (IllegalAccessException e) {

                    throw new ObjectAccessException("Could not call " + result.getClass().getName() + ".readResolve()", e);

                } catch (InvocationTargetException e) {

                    throw new ObjectAccessException("Could not call " + result.getClass().getName() + ".readResolve()", e.getTargetException());

                }

            } else {

                return result;

            }

        }

    }
