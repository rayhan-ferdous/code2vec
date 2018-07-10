    public JApplicationGen(String[] property) {

        if (property.length > 0) {

            JApplicationGen.outputDir = property[0];

            log.debug("Output dir is: " + this.outputDir);

        }

        if (property.length > 1) {

            this.applicationFile = property[1];

            log.debug("project file: : " + this.applicationFile);

        }

    }
