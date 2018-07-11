                    public Object run() throws Exception {

                        if (old != null) {

                            throw new Exception(service.getClass().getName() + " with path '" + p + "' and index [" + service.index() + "] is conflicting with " + old.getClass().getName() + " for the same path and index.");

                        }
