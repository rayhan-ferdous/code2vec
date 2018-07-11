        if ("discountPercentTotal".equals(pColumn)) return new Double(vo.getDiscountPercentTotal());

        if ("vatTotal".equals(pColumn)) return vo.getVatTotal();

        if ("billID".equals(pColumn)) return new Long(vo.getBillID());
