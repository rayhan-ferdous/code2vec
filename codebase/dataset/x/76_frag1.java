        item.set.set("crystallizable", Boolean.valueOf(rset.getString("crystallizable")));

        item.set.set("crystal_count", rset.getInt("crystal_count"));

        item.set.set("sellable", Boolean.valueOf(rset.getString("sellable")));

        item.set.set("dropable", Boolean.valueOf(rset.getString("dropable")));

        item.set.set("destroyable", Boolean.valueOf(rset.getString("destroyable")));

        item.set.set("tradeable", Boolean.valueOf(rset.getString("tradeable")));

        item.set.set("depositable", Boolean.valueOf(rset.getString("depositable")));
