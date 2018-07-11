            File targetFile = new File(targetDir, files[i]);

            FileUtils.copyFile(sourceFile, targetFile);

        }

    }



    private void copyMetaInfFile(final File pSource, final File pTarget, final boolean pExistsBeforeCopying, final String pDescription) throws MojoExecutionException, IOException {

        if (pSource != null && pTarget != null) {

            if (!pSource.exists()) {

                throw new MojoExecutionException("The configured " + pDescription + " could not be found at " + pSource);
