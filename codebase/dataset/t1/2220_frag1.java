            Assert.assertEquals(EventCode.ITEM_SCHEMA_EE, eventType.itemType);

            Assert.assertEquals(4, eventType.getIndex());

            eventTypeList = eventType.getEventTypeList();

            Assert.assertEquals(10, eventTypeList.getLength());

            eventType = eventTypeList.item(0);

            Assert.assertEquals(EventCode.ITEM_SCHEMA_TYPE, eventType.itemType);

            eventType = eventTypeList.item(1);

            Assert.assertEquals(EventCode.ITEM_SCHEMA_NIL, eventType.itemType);

            eventType = eventTypeList.item(2);

            Assert.assertEquals(EventCode.ITEM_SCHEMA_AT, eventType.itemType);
