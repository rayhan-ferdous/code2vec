    public void testCreateAndBasics() throws Exception {

        log.info("========== DependenciesTest.tesCreateAndBasics() ==============");

        log.info("testing dependencies.created(YamFile) and equality methods");

        Dependencies dep2 = Dependencies.get("2");

        for (YamFile yam : wiki1) {

            dep2.created(yam);

        }

        assertEquals("Dependencies linksTo not correct after create", shorthandToPaths("1:[2,3];2:[4,5];3:[5]"), dep2.linksToAsString());

        assertEquals("Dependencies linkedBy not correct after create", shorthandToPaths("2:[1];3:[1];4:[2];5:[2,3]"), dep2.linkedByAsString());

        assertEquals("Dependencies includes not correct after create", shorthandToPaths("1:[2,3,4];2:[4]"), dep2.includesAsString());

        assertEquals("Dependencies includedBy not correct after create", shorthandToPaths("2:[1];3:[1];4:[1,2]"), dep2.includedByAsString());

        assertEquals("Same Dependencies not equal", dep1, dep1);

        assertEquals("Dependencies not equal after get", dep1, Dependencies.get("1"));

        assertEquals("Identical Dependencies not equal", dep1, dep2);

        assertEquals("Identical Dependencies with different hash codes", dep1.hashCode(), dep2.hashCode());

        Dependencies.remove("2");

        assertFalse("Dependencies still exists after removal", Dependencies.exists("2"));

    }
