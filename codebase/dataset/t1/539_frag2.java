        if ("discountPercent".equals(pColumn)) return new Double(vo.getDiscountPercent());

        if ("discountAmount".equals(pColumn)) return vo.getDiscountAmount();

        if ("modified".equals(pColumn)) return vo.getModified();
