                public final com.google.protobuf.Message getResponsePrototype(com.google.protobuf.Descriptors.MethodDescriptor method) {

                    if (method.getService() != getDescriptor()) {

                        throw new java.lang.IllegalArgumentException("Service.getResponsePrototype() given method " + "descriptor for wrong service type.");

                    }

                    switch(method.getIndex()) {

                        case 0:

                            return am.ik.protobuf.TakIOProtos.TakOutput.getDefaultInstance();

                        default:

                            throw new java.lang.AssertionError("Can't get here.");

                    }

                }
