    public DataFlavor[] getTransferDataFlavors() {

        if (transferFlavors == null) {

            if (dch != null) {

                transferFlavors = dch.getTransferDataFlavors();

            } else {

                transferFlavors = new DataFlavor[1];

                transferFlavors[0] = new ActivationDataFlavor(ds.getContentType(), ds.getContentType());

            }

        }

        return transferFlavors;

    }
