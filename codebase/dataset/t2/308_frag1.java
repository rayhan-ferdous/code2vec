    }



    /**

     * Move down.

     * 

     * @param sequence

     * @param resource

     * @return <code>true</code> if moved.

     */

    public boolean downInSequence(Resource parentResource, String resourceUri) {

        Seq seq = model.getSeq(parentResource);

        Resource movedResource = model.getResource(resourceUri);

        int movedResourceIndex = seq.indexOf(movedResource);

        cat.debug("  DOWN: concept index in sequence is " + movedResourceIndex);

        if (movedResourceIndex < seq.size()) {

            com.hp.hpl.jena.rdf.model.Resource auxResource = seq.getResource(movedResourceIndex + 1);

            seq.set(movedResourceIndex, auxResource);

            seq.set(movedResourceIndex + 1, movedResource);

            save();

            cat.debug("  DOWN: after down index: " + seq.indexOf(movedResource));

            return true;

        }

        return false;

    }

}
