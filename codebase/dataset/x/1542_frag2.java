    public static AxisBindingOperation getBindingOperation(AxisBinding binding, AxisOperation operation) {

        QName opName = operation.getName();

        for (Iterator bindingOps = binding.getChildren(); bindingOps.hasNext(); ) {

            AxisBindingOperation bindingOp = (AxisBindingOperation) bindingOps.next();

            if (opName.equals(bindingOp.getName())) {

                return bindingOp;

            }

        }

        return null;

    }
