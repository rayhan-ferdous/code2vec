    public IModelInstanceElement invokeOperation(Operation operation, List<IModelInstanceElement> args) throws OperationNotFoundException, OperationAccessException {

        if (operation == null) {

            throw new IllegalArgumentException("Parameter 'operation' must not be null.");

        } else if (args == null) {

            throw new IllegalArgumentException("Parameter 'args' must not be null.");

        }

        IModelInstanceElement result;

        if (this.myEObject == null) {

            result = this.myFactory.createModelInstanceElement(null, operation.getType());

        } else {

            Method operationMethod;

            int argSize;

            Class<?>[] argumentTypes;

            Object[] argumentValues;

            operationMethod = this.findMethodOfAdaptedObject(operation);

            argumentTypes = operationMethod.getParameterTypes();

            argumentValues = new Object[args.size()];

            argSize = Math.min(args.size(), operation.getSignatureParameter().size());

            for (int index = 0; index < argSize; index++) {

                argumentValues[index] = this.createAdaptedElement(args.get(index), argumentTypes[index]);

            }

            try {

                Object adapteeResult;

                operationMethod.setAccessible(true);

                adapteeResult = operationMethod.invoke(this.myEObject, argumentValues);

                if (adapteeResult instanceof EObject) {

                    try {

                        Type adapteeType = this.myFactory.getTypeUtility().findTypeOfEObjectInModel((EObject) adapteeResult);

                        result = AbstractModelInstance.adaptInvocationResult(adapteeResult, adapteeType, this.myFactory);

                    } catch (TypeNotFoundInModelException e) {

                        throw new OperationAccessException("Result of invocation of Operation '" + operation.getName() + "' could not be adapted to any model type.", e);

                    }

                } else {

                    result = AbstractModelInstance.adaptInvocationResult(adapteeResult, operation.getType(), this.myFactory);

                }

            } catch (IllegalArgumentException e) {

                String msg;

                msg = EcoreModelInstanceTypeMessages.EcoreModelInstanceObject_OperationAccessFailed;

                msg = NLS.bind(msg, operation, e.getMessage());

                throw new OperationAccessException(msg, e);

            } catch (IllegalAccessException e) {

                String msg;

                msg = EcoreModelInstanceTypeMessages.EcoreModelInstanceObject_OperationAccessFailed;

                msg = NLS.bind(msg, operation, e.getMessage());

                throw new OperationAccessException(msg, e);

            } catch (InvocationTargetException e) {

                String msg;

                msg = EcoreModelInstanceTypeMessages.EcoreModelInstanceObject_OperationAccessFailed;

                msg = NLS.bind(msg, operation, e.getMessage());

                throw new OperationAccessException(msg, e);

            }

        }

        return result;

    }
