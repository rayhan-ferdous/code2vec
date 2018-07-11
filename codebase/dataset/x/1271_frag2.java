    public void removeDetalhesMarcados(PlcActionMapping plcMapping, DynaActionForm f) throws PlcException {

        PlcActionMappingDet plcMappingDet = (PlcActionMappingDet) plcMapping;

        try {

            PlcBaseEntity voAux;

            List nomeProps = (List) config.get(plcMappingDet, "detNomePropriedade");

            List nomeClasses = (List) config.get(plcMappingDet, "detVO");

            Iterator i = nomeProps.iterator();

            String nomeProp = "";

            while (i.hasNext()) {

                nomeProp = (String) i.next();

                log.debug("Vai pegar detalhe do form");

                List listaOrigem = (List) f.get(nomeProp);

                if (listaOrigem == null) return;

                List listaAux = new ArrayList();

                if (log.isDebugEnabled()) log.debug("Pegou lista com " + listaOrigem.size() + " elementos");

                Iterator j = listaOrigem.iterator();

                while (j.hasNext()) {

                    voAux = (PlcBaseEntity) j.next();

                    if (voAux.getIndExcPlc() != null && voAux.getIndExcPlc().equals("S")) listaAux.add(voAux);

                }

                int tam = listaOrigem.size();

                int tamExc = listaAux.size();

                listaOrigem.removeAll(listaAux);

                if (tam - tamExc < listaOrigem.size()) {

                    PlcEntityHelper vo = PlcEntityHelper.getInstance();

                    vo.excludeMarked(listaOrigem);

                }

                if (listaAux.size() > 0) f.set(nomeProp, listaOrigem);

            }

        } catch (Exception e) {

            throw new PlcException("jcompany.erros.remove.detalhes", new Object[] { e }, e, log);

        }

    }
