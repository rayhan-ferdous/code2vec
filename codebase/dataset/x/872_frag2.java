        adm("create hist (date, item, id, cost) index(date) key(date,item,id)");

        req("insert{date: 970101, item: \"disk\", id: \"a\", cost: 100} into hist");

        req("insert{date: 970101, item: \"disk\", id: \"e\", cost: 200} into hist");

        req("insert{date: 970102, item: \"mouse\", id: \"c\", cost: 200} into hist");

        req("insert{date: 970103, item: \"pencil\", id: \"e\", cost: 300} into hist");

        adm("create hist2 (date, item, id, cost) key(date) index(id)");

        req("insert{date: 970101, item: \"disk\", id: \"a\", cost: 100} into hist2");

        req("insert{date: 970102, item: \"disk\", id: \"e\", cost: 200} into hist2");

        req("insert{date: 970103, item: \"pencil\", id: \"e\", cost: 300} into hist2");

        adm("create trans (item, id, cost, date) index(item) key(date,item,id)");

        req("insert{item: \"mouse\", id: \"e\", cost: 200, date: 960204} into trans");

        req("insert{item: \"disk\", id: \"a\", cost: 100, date: 970101} into trans");

        req("insert{item: \"mouse\", id: \"c\", cost: 200, date: 970101} into trans");

        req("insert{item: \"eraser\", id: \"c\", cost: 150, date: 970201} into trans");

        adm("create supplier (supplier, name, city) index(city) key(supplier)");

        req("insert{supplier: \"mec\", name: \"mtnequipcoop\", city: \"calgary\"} into supplier");

        req("insert{supplier: \"hobo\", name: \"hoboshop\", city: \"saskatoon\"} into supplier");

        req("insert{supplier: \"ebs\", name: \"ebssail&sport\", city: \"saskatoon\"} into supplier");

        req("insert{supplier: \"taiga\", name: \"taigaworks\", city: \"vancouver\"} into supplier");

        adm("create inven (item, qty) key(item)");

        req("insert{item: \"disk\", qty: 5} into inven");

        req("insert{item: \"mouse\", qty:2} into inven");

        req("insert{item: \"pencil\", qty: 7} into inven");

        adm("create alias(id, name2) key(id)");

        req("insert{id: \"a\", name2: \"abc\"} into alias");

        req("insert{id: \"c\", name2: \"trical\"} into alias");

        adm("create cus(cnum, abbrev, name) key(cnum) key(abbrev)");
