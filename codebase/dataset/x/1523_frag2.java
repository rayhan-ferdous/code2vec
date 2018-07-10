            return this.filter.accept(new File(dir.getPath() + File.pathSeparator + name));

        }

    }



    private File acceptChoice(final File file) {

        if (file == null) {

            return null;

        } else {

            return new File(this.xp.getFilename(file.getPath(), this.getFileFilter()));
