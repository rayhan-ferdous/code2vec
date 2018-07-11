                if (numIterations == -1 || numIterations > modelSelection[currentBag].numModels) maxIters = modelSelection[currentBag].numModels;

                modelSelection[currentBag].eachModel(maxIters);

            } else if (cmd.equals("bm")) {

                modelSelection[currentBag].eachModel(1);
