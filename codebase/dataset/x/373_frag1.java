            if (props.containsKey("ogg.bitrate.max.bps")) {

                maxbitrate = ((Integer) props.get("ogg.bitrate.max.bps")).intValue();

            }

            if (props.containsKey("ogg.bitrate.min.bps")) {

                minbitrate = ((Integer) props.get("ogg.bitrate.min.bps")).intValue();

            }

            if (props.containsKey("ogg.version")) {
