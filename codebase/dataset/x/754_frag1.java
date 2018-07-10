            out.write("\tfunction gerarServico(){\r\n");

            out.write("\t\tvar form = document.forms[0];\r\n");

            out.write("\t\tform.metodo.value = 'gerar';\r\n");

            out.write("\t\tform.submit();\r\n");

            out.write("\t}\r\n");

            out.write("\t\r\n");

            out.write("\tfunction carregarMascaras() {\r\n");

            out.write("\t\t\r\n");

            out.write("\t\t\r\n");

            out.write("\t\tvalorMask = new Mask(\"#.00\", \"number\");\r\n");

            out.write("\t\tvalorMask.attach(document.getElementById('campoValorTotal'));\r\n");

            out.write("\t\t\r\n");

            out.write("\t\tquantidadeParcelasMask = new Mask(\"#\", \"number\");\r\n");

            out.write("\t\tquantidadeParcelasMask.attach(document.getElementById('campoQuantidadeParcelas'));\r\n");

            out.write("\t\t\r\n");

            out.write("\t\tmesMask = new Mask(\"##\", \"number\");\r\n");

            out.write("\t\tmesMask.attach(document.getElementById('campoMesVencimentoPrimeiraParcela'));\r\n");

            out.write("\t\t\r\n");

            out.write("\t\tanoMask = new Mask(\"####\", \"number\");\r\n");

            out.write("\t\tanoMask.attach(document.getElementById('campoAnoVencimentoPrimeiraParcela'));\r\n");

            out.write("\t\t\r\n");

            out.write("\t\t\r\n");

            out.write("\t\tmesMask.attach(document.getElementById('quantidadePrimeirasParcelas'));\r\n");

            out.write("\t\t\r\n");

            out.write("\t\tmesMask.attach(document.getElementById('campoQuantidadeParcelas'));\r\n");

            out.write("\t\t\r\n");

            out.write("\t\t\r\n");

            out.write("\t\tvalorMask.attach(document.getElementById('campoValorTotal'));\r\n");

            out.write("\t\t\t\r\n");

            out.write("\t\tvalorMask.attach(document.getElementById('valorPrimeirasParcelas'));\r\n");

            out.write("\t\t\r\n");

            out.write("\t}\r\n");

            out.write("\t\r\n");

            out.write("\t\r\n");

            out.write("\tfunction inicializaCombo(){\r\n");

            out.write("\t\tvar a = document.getElementById('tipo').value;\r\n");

            out.write("\t\tdocument.forms[0].radioTipoServico[a].checked = 1;\r\n");

            out.write("\t}\r\n");

            out.write("\t\r\n");

            out.write("\tfunction esconde(){\r\n");

            out.write("\t\tfor(i =0; i <= 4; i++){\r\n");

            out.write("\t\t\tif(document.forms[0].radioTipoServico[i].checked){\r\n");

            out.write("\t\t\t\tdocument.getElementById('tipo').value = i;\r\n");

            out.write("\t\t\t\t//alert(i);\r\n");

            out.write("\t\t\t}\r\n");

            out.write("\t\t}\r\n");

            out.write("\t\t\r\n");

            out.write("\t\t\r\n");

            out.write("\t\tif(document.getElementById('tipo').value == 3){\r\n");

            out.write("\t\t\tdocument.getElementById('parcelasDiv').style.display='none';\r\n");

            out.write("\t\t\tdocument.getElementById('descricao').style.display='none';\r\n");
