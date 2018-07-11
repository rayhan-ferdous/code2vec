                value[2] = BAC.autoEstimateCph ? summary.cphMethyLevel : BAC.forceCph;

            } else if (tmpKey[0].equalsIgnoreCase("GCH")) {

                value[2] = BAC.autoEstimateGch ? sumCytosine.gchMethyLevel : BAC.forceGch;

            } else if (tmpKey[0].equalsIgnoreCase("CCH")) {

                value[2] = BAC.autoEstimateCch ? sumCytosine.cchMethyLevel : BAC.forceCch;

            } else if (tmpKey[0].equalsIgnoreCase("WCH")) {

                value[2] = BAC.autoEstimateWch ? sumCytosine.wchMethyLevel : BAC.forceWch;

            } else if (tmpKey[0].equalsIgnoreCase("GCG")) {

                value[2] = BAC.autoEstimateGcg ? sumCytosine.gcgMethyLevel : BAC.forceGcg;

            } else if (tmpKey[0].equalsIgnoreCase("CCG")) {

                value[2] = BAC.autoEstimateCcg ? sumCytosine.ccgMethyLevel : BAC.forceCcg;

            } else if (tmpKey[0].equalsIgnoreCase("WCG")) {

                value[2] = BAC.autoEstimateWcg ? sumCytosine.wcgMethyLevel : BAC.forceWcg;

            } else {
