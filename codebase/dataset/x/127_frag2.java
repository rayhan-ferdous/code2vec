    public AudioFileFormat.Type[] getAudioFileTypes(AudioInputStream audioInputStream) {

        AudioFormat format = audioInputStream.getFormat();

        ArraySet res = new ArraySet();

        Iterator it = m_audioFileTypes.iterator();

        while (it.hasNext()) {

            AudioFileFormat.Type thisType = (AudioFileFormat.Type) it.next();

            if (isAudioFormatSupportedImpl(format, thisType)) {

                res.add(thisType);

            }

        }

        return (AudioFileFormat.Type[]) res.toArray(NULL_TYPE_ARRAY);

    }
