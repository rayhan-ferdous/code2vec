    private void closeVideo() {

        if (container != null) {

            container.close();

            container = null;

        }

        if (videoCoder != null) {

            videoCoder.close();

            videoCoder = null;

        }

        if (resampler != null) {

            resampler.delete();

            resampler = null;

        }

        if (converter != null) {

            converter.delete();

            converter = null;

        }

        if (packet != null) {

            packet.delete();

            packet = null;

        }

    }
