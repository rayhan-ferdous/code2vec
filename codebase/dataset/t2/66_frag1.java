        if (srcFile.isDirectory()) {

            throw new IOException("Source '" + srcFile + "' exists but is a directory");

        }

        if (srcFile.getCanonicalPath().equals(destFile.getCanonicalPath())) {

            throw new IOException("Source '" + srcFile + "' and destination '" + destFile + "' are the same");

        }

        if (destFile.getParentFile() != null && destFile.getParentFile().exists() == false) {
