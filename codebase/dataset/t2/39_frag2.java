        writeStartElement(xtw, "NAME", Util.nvl(((Fermentation) beerxml.getRecipe().getFermentation().get(h)).getName()), false);

        writeStartElement(xtw, "VERSION", Util.format0(Util.nvl(((Fermentation) beerxml.getRecipe().getFermentation().get(h)).getVersion(), Double.valueOf(1))), false);

        writeStartElement(xtw, "NOTES", Util.nvl(((Fermentation) beerxml.getRecipe().getFermentation().get(h)).getNotes()), false);
