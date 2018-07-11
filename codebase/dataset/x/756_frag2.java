    public ServiceDescription describe() {

        ServiceDescription.Builder sd = new ServiceDescription.Builder(NAME, Validate.class.getCanonicalName());

        sd.classname(this.getClass().getCanonicalName());

        sd.description("Validation service based on PngCheck.");

        sd.author("Fabian Steeg");

        sd.inputFormats(PNG_PRONOM.toArray(new URI[] {}));

        sd.tool(Tool.create(null, "PngCheck", null, null, "http://www.libpng.org/pub/png/apps/pngcheck.html"));

        sd.serviceProvider("The Planets Consortium");

        return sd.build();

    }
