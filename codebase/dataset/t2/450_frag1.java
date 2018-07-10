    private boolean decodeVideoPacket(StreamState state) {

        boolean complete = false;

        while (!state.isPacketEmpty()) {

            int decodedBytes = videoCoder.decodeVideo(videoPicture, state.getPacket(), state.getOffset());

            if (decodedBytes < 0) {

                state.releasePacket();

                break;

            }

            state.updateTimestamps();

            state.consume(decodedBytes);

            if (videoPicture.isComplete()) {

                if (videoConverter != null) {

                    currentImg = videoConverter.toImage(videoPicture);

                }

                complete = true;

                break;

            }

        }

        return complete;

    }
