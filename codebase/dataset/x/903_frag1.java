    public int getSymmetryTranslation(int symop, int[] cellRange, int nOps) {

        int pt = symop;

        for (int i = 0; i < cellRange.length; i++) if (atomSymmetry.get(pt += nOps)) return cellRange[i];

        return 0;

    }



    /**

    * Looks for a match in the cellRange list for this atom within the specified translation set

    * select symop=0NNN for this

    * 

    * @param cellNNN

    * @param cellRange

    * @param nOps

    * @return     matching cell number, if applicable

    */

    public int getCellTranslation(int cellNNN, int[] cellRange, int nOps) {

        int pt = nOps;

        for (int i = 0; i < cellRange.length; i++) for (int j = 0; j < nOps; j++, pt++) if (atomSymmetry.get(pt) && cellRange[i] == cellNNN) return cellRange[i];

        return 0;

    }



    String getSymmetryOperatorList() {
