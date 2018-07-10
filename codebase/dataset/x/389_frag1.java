            return false;

        } else {

            for (int i = 0; i < categories.length; i++) {

                if (categories[i].equals(category)) {

                    return true;

                }

            }

            return false;

        }

    }



    /**

     * Sets the key that identifies this item in the PIM database

     * @param key an Object used to index this item

     */

    void setKey(Object key) {

        this.key = key;
