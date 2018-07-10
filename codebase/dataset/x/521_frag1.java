    ImageTexture(Loader loader, MFString url, SFBool repeatS, SFBool repeatT) {

        super(loader);

        this.url = url;

        this.repeatS = repeatS;

        this.repeatT = repeatT;

        initFields();

    }
