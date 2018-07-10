    public Recording(String channelName, XMLGregorianCalendar startDate, Duration duration, Duration totalDuration, String title, String description, String shortText, String id, List<StreamInfo> streamInfos, String path, int parts, boolean ts, String streamUrl) {

        this.channelName = channelName;

        this.startDate = startDate;

        this.duration = duration;

        this.totalDuration = totalDuration;

        this.title = title;

        this.description = description;

        this.shortText = shortText;

        this.id = id;

        this.streamInfos = streamInfos;

        this.path = path;

        this.parts = parts;

        this.ts = ts;

        this.streamUrl = streamUrl;

    }
