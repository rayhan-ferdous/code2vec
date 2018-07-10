    public void Recombine() {

        int NumRecParentCnt, FirstParent, SecondParent, RecIndex, LineInChildrenMatrix, ReadIndex, WriteIndex, VectorNodeValue;

        int l;

        boolean ExistFlag;

        Random r = new Random();

        NumRecParentCnt = 0;

        RecIndex = 0;

        WriteIndex = 0;

        LineInChildrenMatrix = 0;

        ReadIndex = 0;

        l = 0;

        ExistFlag = true;

        this.NumRecParent = (int) Math.floor(Math.sqrt(NumVertices)) - this.NumMutParent;

        while (NumRecParentCnt < this.NumRecParent) {

            FirstParent = Math.abs(r.nextInt()) % (int) Math.floor(Math.sqrt(NumVertices));

            SecondParent = Math.abs(r.nextInt()) % (int) Math.floor(Math.sqrt(NumVertices));

            while (FirstParent == SecondParent) {

                SecondParent = Math.abs(r.nextInt()) % (int) Math.floor(Math.sqrt(NumVertices));

            }

            RecIndex = Math.abs(r.nextInt()) % NumVertices;

            for (int i = 0; i < RecIndex; i++) {

                LineInChildrenMatrix = NumRecParentCnt + this.NumMutParent;

                ChildrenMatrix[i][LineInChildrenMatrix] = ParentMatrix[i][FirstParent];

            }

            WriteIndex = RecIndex;

            for (int i = RecIndex; i < NumVertices; i++) {

                l = 0;

                ExistFlag = true;

                while (ExistFlag == true) {

                    ReadIndex = (l + WriteIndex) % NumVertices;

                    VectorNodeValue = ParentMatrix[ReadIndex][SecondParent];

                    ExistFlag = false;

                    for (int k = 0; k < WriteIndex; k++) {

                        if (ChildrenMatrix[k][LineInChildrenMatrix] == VectorNodeValue) {

                            ExistFlag = true;

                            break;

                        }

                    }

                    l++;

                }

                ChildrenMatrix[WriteIndex][LineInChildrenMatrix] = ParentMatrix[ReadIndex][SecondParent];

                WriteIndex++;

            }

            NumRecParentCnt++;

        }

        NumRecParentCnt = 0;

    }
