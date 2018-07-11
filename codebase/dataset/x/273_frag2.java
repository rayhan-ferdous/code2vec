    private boolean openAudioFile(File audioFile) {

        try {

            aff = AudioSystem.getAudioFileFormat(audioFile);

            ais = AudioSystem.getAudioInputStream(audioFile);

            af = ais.getFormat();

        } catch (Exception ex) {

            System.out.println(ex);

            return false;

        }

        return true;

    }
