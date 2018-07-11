        for (int ChildrenVector = 0; ChildrenVector < NumChildrenSolutions; ChildrenVector++) {

            if (ChildrenMatrix[NumVertices][ChildrenVector] == 0) {

                for (int VectorElement = 0; VectorElement < NumVertices; VectorElement++) {

                    SolutionVector[VectorElement] = ChildrenMatrix[VectorElement][ChildrenVector];

                }

                ChildrenMatrix[NumVertices][ChildrenVector] = CalculateTreeWidth(Graph, SolutionVector, 0);

                if ((lokalMinTreeWidth == 0) || ChildrenMatrix[NumVertices][ChildrenVector] < lokalMinTreeWidth) {

                    lokalMinTreeWidth = ChildrenMatrix[NumVertices][ChildrenVector];

                }

            }

        }

        for (int ParentVector = 0; ParentVector < NumParentSolutions; ParentVector++) {
