    }



    /**

     * Move down.

     * 

     * @param parentResource

     *            the Resource

     * @param resourceUri

     *            the resource uri

     * @return <code>true</code> if moved.

     */

    public boolean downInSequence(Resource parentResource, String resourceUri) {

        Seq seq = model.getSeq(parentResource);

        Resource movedResource = model.getResource(resourceUri);

        int movedResourceIndex = seq.indexOf(movedResource);

        logger.debug("  DOWN: concept index in sequence is " + movedResourceIndex);

        if (movedResourceIndex < seq.size()) {

            com.hp.hpl.jena.rdf.model.Resource auxResource = seq.getResource(movedResourceIndex + 1);

            seq.set(movedResourceIndex, auxResource);

            seq.set(movedResourceIndex + 1, movedResource);

            save();

            logger.debug("  DOWN: after down index: " + seq.indexOf(movedResource));

            return true;

        }

        return false;

    }

}
