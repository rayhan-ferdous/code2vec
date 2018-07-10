        public final void callMethod(com.google.protobuf.Descriptors.MethodDescriptor method, com.google.protobuf.RpcController controller, com.google.protobuf.Message request, com.google.protobuf.RpcCallback<com.google.protobuf.Message> done) {

            if (method.getService() != getDescriptor()) {

                throw new java.lang.IllegalArgumentException("Service.callMethod() given method descriptor for wrong " + "service type.");

            }

            switch(method.getIndex()) {

                case 0:

                    this.testMethod(controller, (com.googlecode.protobuf.socketrpc.TestProtos.Request) request, com.google.protobuf.RpcUtil.<com.googlecode.protobuf.socketrpc.TestProtos.Response>specializeCallback(done));

                    return;

                default:

                    throw new java.lang.AssertionError("Can't get here.");

            }

        }
