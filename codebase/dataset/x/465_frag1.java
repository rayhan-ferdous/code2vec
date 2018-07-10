        public DataRow(String name, String unit, double lowerThresh, double upperThresh) {

            this.name = name;

            this.unit = unit;

            this.minThresh = lowerThresh;

            this.maxThresh = upperThresh;

            clearData();

        }
