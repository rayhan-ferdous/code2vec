    public static boolean setExecutable(final File file, final boolean executable, final boolean ownerOnly) throws IOException {

        if (file == null) return false;

        if (!file.exists() || !file.isFile()) throw new FileNotFoundException(file.getAbsolutePath());

        final String cmd = "chmod " + (ownerOnly ? "u+x " : "ugo+x ") + file.getAbsolutePath();

        ProcessUtils.exec(cmd, false);

        return true;

    }



    /**

   * Set executable bits on file on *nix.

   * @param file File to handle

   * @param ownerOnly If true, the execute permission applies only to the

   *          owner's execute permission; otherwise, it applies to everybody. If

   *          the underlying file system can not distinguish the owner's execute

   *          permission from that of others, then the permission will apply to

   *          everybody, regardless of this value.

   * @return true if and only if the operation succeeded

   * @throws IOException

   */

    public static boolean setExecutable(final File file, boolean executable) throws IOException {

        return setExecutable(file, executable, false);

    }



    /**

   * Set readable bits on file on *nix.

   * @param file File to handle

   * @param readable If true, sets the access permission to allow read

   *          operations; if false to disallow execute operations

   * @param ownerOnly If true, the execute permission applies only to the

   *          owner's execute permission; otherwise, it applies to everybody. If

   *          the underlying file system can not distinguish the owner's execute

   *          permission from that of others, then the permission will apply to

   *          everybody, regardless of this value.

   * @return true if and only if the operation succeeded

   * @throws IOException

   */

    public static boolean setReadable(final File file, final boolean readable, final boolean ownerOnly) throws IOException {

        if (file == null) return false;

        if (!file.exists() || !file.isFile()) throw new FileNotFoundException(file.getAbsolutePath());

        final String cmd = "chmod " + (ownerOnly ? "u+r " : "ugo+r ") + file.getAbsolutePath();

        ProcessUtils.exec(cmd, true);

        return true;

    }



    /**

   * Set readable bits on file on *nix.

   * @param file File to handle

   * @param readable If true, sets the access permission to allow read

   *          operations; if false to disallow execute operations

   * @return true if and only if the operation succeeded

   * @throws IOException

   */

    public static boolean setReadable(final File file, boolean readable) throws IOException {

        return setReadable(file, readable, true);

    }



    /**

   * Set writable bits on file on *nix.

   * @param file File to handle

   * @param writable If true, sets the access permission to allow read

   *          operations; if false to disallow execute operations

   * @param ownerOnly If true, the execute permission applies only to the

   *          owner's execute permission; otherwise, it applies to everybody. If

   *          the underlying file system can not distinguish the owner's execute

   *          permission from that of others, then the permission will apply to

   *          everybody, regardless of this value.

   * @return true if and only if the operation succeeded

   * @throws IOException

   */

    public static boolean setWritable(final File file, final boolean writable, final boolean ownerOnly) throws IOException {
