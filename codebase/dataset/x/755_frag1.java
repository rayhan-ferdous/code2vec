        for (int i = 0; i < members.length; i++) {

            if (members[i].tags("deprecated").length == 0) {

                list.add(members[i]);

            }

        }

        Collections.sort(list);

        return list;

    }



    /**

     * Return the list of ProgramElementDoc objects as Array.

     */

    public static ProgramElementDoc[] toProgramElementDocArray(List list) {

        ProgramElementDoc[] pgmarr = new ProgramElementDoc[list.size()];
