    private void loadExports(long address) {

        if (exports == null) {

            exports = new HeaderImageExportDirectory();

            try {

                LittleEndianFile file = new LittleEndianFile(baseAddress + (int) address);

                exports.load(file);

            } catch (Exception e) {

                exports = null;

                e.printStackTrace();

            }

        }

    }
