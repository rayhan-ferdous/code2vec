    public void onDisable() {

        messages.stop(true);

        daemon.stop(true);

        users.save();

        protections.save();

        warps.save();

        locks.save();

        roleplay.save();

        System.out.println("### BasicBukkit plugin disabled.");

    }
