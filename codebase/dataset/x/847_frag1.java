            return true;

        }



        boolean LoadUnloadMedia(short subUnit, boolean unload) {

            if (subUnit >= numDrives) return false;

            dinfo[subUnit].lastResult = cdrom[subUnit].LoadUnloadMedia(unload);

            return dinfo[subUnit].lastResult;

        }



        boolean SendDriverRequest(int drive, int data) {

            short subUnit = GetSubUnit(drive);
