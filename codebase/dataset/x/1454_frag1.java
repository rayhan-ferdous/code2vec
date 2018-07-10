    public static void writeNode(Node n, XMLStreamWriter writer, boolean repairing) throws XMLStreamException {

        if (n instanceof Element) {

            writeElement((Element) n, writer, repairing);

        } else if (n instanceof Text) {

            writer.writeCharacters(((Text) n).getNodeValue());

        } else if (n instanceof CDATASection) {

            writer.writeCData(((CDATASection) n).getData());

        } else if (n instanceof Comment) {

            writer.writeComment(((Comment) n).getData());

        } else if (n instanceof EntityReference) {

            writer.writeEntityRef(((EntityReference) n).getNodeValue());

        } else if (n instanceof ProcessingInstruction) {

            ProcessingInstruction pi = (ProcessingInstruction) n;

            writer.writeProcessingInstruction(pi.getTarget(), pi.getData());

        }

    }
