    public void testFixtureWithDualChannels() {

        FixtureDefinition definition = new FixtureDefinition();

        definition.addAttribute("Intensity", "1");

        definition.addAttribute("Pan", "2,3");

        definition.addAttribute("Tilt", "4,5");

        fixture = new Fixture(definition);

        assertChannels("Intensity", 0);

        assertChannels("Pan", 0, 0);

        assertChannels("Tilt", 0, 0);

        fixture.setAddress(11);

        assertChannels("Intensity", 11);

        assertChannels("Pan", 12, 13);
