        } else if (vElementName.equalsIgnoreCase("LAUTER_DEADSPACE")) {

            this.beerXml.getRecipe().getEquipment().setLauter_Deadspace(Util.str2dbl(vStringElementValue));

        } else if (vElementName.equalsIgnoreCase("TOP_UP_KETTLE")) {

            this.beerXml.getRecipe().getEquipment().setTop_Up_Kettle(Util.str2dbl(vStringElementValue));

        } else if (vElementName.equalsIgnoreCase("HOP_UTILIZATION")) {

            this.beerXml.getRecipe().getEquipment().setHop_Utilization(Util.str2dbl(vStringElementValue));

        } else if (vElementName.equalsIgnoreCase("NOTES")) {
