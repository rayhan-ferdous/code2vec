            if ("itemCount".equals(pColumn)) return new Long(fTotal.getItemCount());

            if ("shippingDate".equals(pColumn)) return fTotal.getShippingDate();

            if ("price".equals(pColumn)) return fTotal.getPrice();
