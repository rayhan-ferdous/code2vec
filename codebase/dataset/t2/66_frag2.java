        if (srcDir.isDirectory() == false) {

            throw new IOException("Source '" + srcDir + "' exists but is not a directory");

        }

        if (srcDir.getCanonicalPath().equals(destDir.getCanonicalPath())) {

            throw new IOException("Source '" + srcDir + "' and destination '" + destDir + "' are the same");

        }

        List exclusionList = null;
