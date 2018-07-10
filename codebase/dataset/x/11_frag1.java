        if (allow(ID3V2, type)) {

            if (id3v2.tagExists()) {

                size += id3v2.getSize();

            }

        }

        return size;

    }



    /**

   * Checks whether it is ok to read or write from the tag version specified

   * based on the tagType passed to the constructor. The tagVersion parameter

   * should be either ID3V1 or ID3V2.

   *

   *@param tagVersion  the id3 version to check

   *@return            true if it is ok to proceed with the read/write

   */

    private boolean allow(int tagVersion) {

        return this.allow(tagVersion, tagType);
