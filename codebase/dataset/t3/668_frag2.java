        for (final Hauptgruppe hauptgruppe : hauptgruppen) {

            writer.startElement("option", this);

            writer.writeAttribute("value", hauptgruppe.getId(), null);

            if (hauptgruppe.equals(selectedHauptgruppe)) {

                writer.writeAttribute("selected", "selected", null);

            }

            writer.writeText(hauptgruppe.getHauptgruppeName(), null);

            writer.endElement("option");

        }

        writer.endElement("select");

        this.renderTableFooter(writer);

    }
