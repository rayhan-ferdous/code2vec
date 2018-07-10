    public ProfileResultLeftPanel(ProfileController controller, Profile profile, int startingMADs, String imageFilename, ProfileResultFrame profileResultFrame) {

        this.controller = controller;

        this.profile = profile;

        this.imageFilename = imageFilename;

        this.profileResultFrame = profileResultFrame;

        initComponents(startingMADs);

    }
