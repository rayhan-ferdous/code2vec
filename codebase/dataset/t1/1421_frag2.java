            while (parentPart != null && !associatedParts.contains(parentPart)) {

                parentPart = parentPart.getParent();

            }

            if (parentPart == null) {

                editPartCollector.add(nextPart);

            }

        }

        if (intialNumOfEditParts == editPartCollector.size()) {

            if (!associatedParts.isEmpty()) {

                editPartCollector.add(associatedParts.iterator().next());

            } else {

                if (element.eContainer() != null) {

                    return findElementsInDiagramByID(diagramPart, element.eContainer(), editPartCollector);

                }

            }

        }

        return editPartCollector.size() - intialNumOfEditParts;

    }
