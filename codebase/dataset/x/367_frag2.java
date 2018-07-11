            m_audioInputStream = AppletMpegSPIWorkaround.getAudioInputStream(url);

            m_audioFileFormat = AppletMpegSPIWorkaround.getAudioFileFormat(url);

        } else {

            m_audioInputStream = AudioSystem.getAudioInputStream(url);

            m_audioFileFormat = AudioSystem.getAudioFileFormat(url);

        }

    }



    /**

     * Inits Audio ressources from AudioSystem.<br>

     * DateSource must be present.

     */

    protected final void initLine() throws LineUnavailableException {

        if (m_line == null) {

            createLine();

            trace(1, "---", "Create Line OK ");
