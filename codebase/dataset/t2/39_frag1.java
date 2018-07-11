        writeStartElement(xtw, "NAME", Util.nvl(((Water) beerxml.getRecipe().getWaters().get(h)).getName()), false);

        writeStartElement(xtw, "VERSION", Util.format0(Util.nvl(((Water) beerxml.getRecipe().getWaters().get(h)).getVersion(), Double.valueOf(1))), false);

        writeStartElement(xtw, "AMOUNT", Util.nvl(((Water) beerxml.getRecipe().getWaters().get(h)).getAmount()), false);
