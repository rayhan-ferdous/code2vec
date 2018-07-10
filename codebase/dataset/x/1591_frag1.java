    public static Revision buildRevision(PathNode path, Object[] orig, Object[] rev) {

        if (path == null) throw new IllegalArgumentException("path is null");

        if (orig == null) throw new IllegalArgumentException("original sequence is null");

        if (rev == null) throw new IllegalArgumentException("revised sequence is null");

        Revision revision = new Revision();

        if (path.isSnake()) path = path.prev;

        while (path != null && path.prev != null && path.prev.j >= 0) {

            if (path.isSnake()) throw new IllegalStateException("bad diffpath: found snake when looking for diff");

            int i = path.i;

            int j = path.j;

            path = path.prev;

            int ianchor = path.i;

            int janchor = path.j;

            Delta delta = Delta.newDelta(new Chunk(orig, ianchor, i - ianchor), new Chunk(rev, janchor, j - janchor));

            revision.insertDelta(delta);

            if (path.isSnake()) path = path.prev;

        }

        return revision;

    }

}
