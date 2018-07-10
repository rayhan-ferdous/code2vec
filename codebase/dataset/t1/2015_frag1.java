        stmtDestino.setString(17, (pFiltro.setoresComApenasUmSubsetor ? "X" : ""));

        stmtDestino.setString(18, (pFiltro.setoresComVariosSubsetores ? "X" : ""));

        stmtDestino.setInt(19, (pFiltro.setor != null ? pFiltro.setor.obterCodigo() : null));

        stmtDestino.setString(20, (pFiltro.subsetoresComApenasUmSegmento ? "X" : ""));

        stmtDestino.setString(21, (pFiltro.subsetoresComVariosSegmentos ? "X" : ""));

        stmtDestino.setInt(22, (pFiltro.subsetor != null ? pFiltro.subsetor.obterCodigo() : null));

        stmtDestino.setString(23, (pFiltro.segmentosComApenasUmaEmpresa ? "X" : ""));

        stmtDestino.setString(24, (pFiltro.segmentosComVariasEmpresas ? "X" : ""));

        stmtDestino.setInt(25, (pFiltro.segmento != null ? pFiltro.segmento.obterCodigo() : null));

        stmtDestino.setString(26, (pFiltro.empresasComApenasUmPapel ? "X" : ""));
