            theDoc.myWindow().BPM1List.setSelectedValue(bpm1, true);

        }

        if (daNodes.hasAttribute("BPM2")) {

            bpm2 = theDoc.getAccelerator().getNode(daNodes.stringValue("BPM2"));

            theDoc.myWindow().BPM2List.setSelectedValue(bpm2, true);
