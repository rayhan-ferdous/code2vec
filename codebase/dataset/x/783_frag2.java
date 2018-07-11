        Long[] nums = new Long[names.length];

        for (int i = 0; i < nums.length; i += 1) {

            nums[i] = getNumFromName(names[i]);

        }

        return nums;

    }



    /**

     * Get the next file number before/after currentFileNum. 

     * @param currentFileNum the file we're at right now. Note that

     * it may not exist, if it's been cleaned and renamed.

     * @param forward if true, we want the next larger file, if false

     * we want the previous file

     * @return null if there is no following file, or if filenum doesn't exist

     */

    public Long getFollowingFileNum(long currentFileNum, boolean forward) {

        String[] names = listFiles(JE_SUFFIXES);
