            exiEvent = exiEventList.get(pos++);

            Assert.assertEquals(EXIEvent.EVENT_CH, exiEvent.getEventVariety());

            Assert.assertEquals("", exiEvent.getCharacters().makeString());

            exiEvent = exiEventList.get(pos++);

            Assert.assertEquals(EXIEvent.EVENT_EE, exiEvent.getEventVariety());

            exiEvent = exiEventList.get(pos++);

            Assert.assertEquals(EXIEvent.EVENT_SE, exiEvent.getEventVariety());
