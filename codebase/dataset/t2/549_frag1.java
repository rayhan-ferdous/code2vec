        if (bDeleteOriginalFile) try {

            if (!(new File(sSource)).delete()) Log.log(Log.WARNING, "lazyj.Utils", "resize: could not delete original file (" + sSource + ")");

        } catch (SecurityException se) {

            Log.log(Log.ERROR, "lazyj.Utils", "resize: security constraints prevents file deletion");
