    protected ZipFileObject(ZipFileSystem zfs, ZipEntry zipEntry, ZipFileObject par, String name) {

        this.zfs = zfs;

        this.zipEntry = zipEntry;

        this.parent = par;

        this.name = name;

        children = new ArrayList<ZipFileObject>();

    }
