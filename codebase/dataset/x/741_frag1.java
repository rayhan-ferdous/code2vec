    private void displayCurrentAudioFormat() {

        if (last_recorded_audio != null) {

            String data = AudioMethods.getAudioFormatData(last_recorded_audio.getFormat());

            JOptionPane.showMessageDialog(null, data, "Current Audio Encoding", JOptionPane.INFORMATION_MESSAGE);

        } else JOptionPane.showMessageDialog(null, "No audio has been stored.", "WARNING", JOptionPane.ERROR_MESSAGE);
