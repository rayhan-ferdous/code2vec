                public Object execute(Object arg) {

                    if (logger.isDebugEnabled()) {

                        logger.debug("$UnaryFunction.execute(Object) - start");

                    }

                    System.out.println(arg);

                    if (logger.isDebugEnabled()) {

                        logger.debug("$UnaryFunction.execute(Object) - end");

                    }

                    return null;

                }
