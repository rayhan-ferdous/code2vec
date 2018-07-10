    private void commitTranscripts(CurationSet curation, File dir, Vector omit) {

        DataOutputStream dos = openGenbankFile(dir, outputFileStem(curation) + ".rna");

        try {

            StrandedFeatureSetI annots = curation.getAnnots();

            for (int i = 0; i < annots.size(); i++) {

                AnnotatedFeatureI gene = (AnnotatedFeatureI) annots.getFeatureAt(i);

                dos.writeBytes(writeTranscript(gene, dir, omit));

            }

            try {

                dos.close();

            } catch (Exception ex) {

                logger.error("caught exception closing " + genbank_file.getName(), ex);

            }

        } catch (Exception ex) {

            logger.error("caught exception writing " + genbank_file.getName(), ex);

        }

    }



    private void commitPeptides(CurationSet curation, File dir, Vector omit) {
