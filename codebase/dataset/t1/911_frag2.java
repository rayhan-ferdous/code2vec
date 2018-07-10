    public final void estimateInitialFillingPerStationAndClassForN(final double loadfactor) {

        for (int c = 0; c < this.inputparameters.getCustomerClasses().size(); c++) {

            final Class customerClass = (Class) this.inputparameters.getCustomerClasses().get(c);

            final int kR = this.inputparameters.numberOfStationsWithServiceDemandGreaterZeroForClass(customerClass.getClassID());

            final double nR = customerClass.getPopulation();

            final double estimator = nR / kR;

            for (int q = 0; q < this.inputparameters.getStations().size(); q++) {

                final AnalyticQNStation station = (AnalyticQNStation) this.inputparameters.getStations().get(q);

                this.tableMVAEstimatorPerStationAndClass.write(station.getStationID(), customerClass.getClassID(), estimator);

            }

        }

    }
