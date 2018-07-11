    @SuppressWarnings("unchecked")

    @Test(expected = InfeasibleEvaluatorException.class)

    public final void testClassCorrNoNeighboursInOnt1() throws Exception {

        initWithCorrespondences();

        OWLEntity ent1 = getEntity("http://example.org/similarityFloodingTest1.owl#NoSuperClassNoSubClass", OWLClass.class, ontology1);

        OWLEntity ent2 = getEntity("http://example.org/similarityFloodingTest2.owl#OneSuperClass", OWLClass.class, ontology2);

        Correspondence<? extends OWLEntity> corr = fac.createCorrespondence(ent1, ent2);

        baseMatcher.getEvaluation(corr);

    }
