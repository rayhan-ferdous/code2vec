    public RecordableClip(File file, AudioFormat format) throws IOException, AudioException {

        if (file == null) currentFile = File.createTempFile("audioFile", ".wav"); else currentFile = file;

        if (!currentFile.exists()) {

            IOUtils.create(currentFile);

        }

        int len = (int) Math.ceil(format.getFrameSize() * format.getFrameRate() * DEFAULT_PROMPT_MINUTES_LEN);

        streamData = new ByteArray(len);

        streamLength = IOUtils.load(currentFile, streamData);

        audioFormat = format;

        LineAndStream recordingStream = AudioUtils.getRecordingStream(audioFormat);

        recordThread = new RecordThread(recordingStream);

    }
