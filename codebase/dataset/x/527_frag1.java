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

            out.write("\t\t}else if(document.getElementById('tipo').value == 4){\r\n");

            out.write("\t\t\tdocument.getElementById('descricao').style.display='block';\r\n");

            out.write("\t\t}\r\n");

            out.write("\t}\r\n");

            out.write("\t\r\n");

            out.write("\t\r\n");

            out.write("\tfunction mudaOpcao(){\r\n");

            out.write("\t\tfor(i =0; i <= 4; i++){\r\n");

            out.write("\t\t\tif(document.forms[0].radioTipoServico[i].checked){\r\n");

            out.write("\t\t\t\tdocument.getElementById('tipo').value = i;\r\n");

            out.write("\t\t\t\t//alert(i);\r\n");

            out.write("\t\t\t}\r\n");

            out.write("\t\t} \r\n");

            out.write("\t\tif(document.forms[0].radioTipoServico[3].checked){\r\n");

            out.write("\t\t\t//document.getElementById('campoQuantidadeParcelas').style.display='none';\r\n");

            out.write("\t\t//\tdocument.getElementById('labelparcelas').style.display='none';\r\n");

            out.write("\t\t\tdocument.getElementById('parcelasDiv').style.display='none';\r\n");

            out.write("\t\t\tdocument.getElementById('valorMensal').style.display='block';\r\n");

            out.write("\t\t\tdocument.getElementById('valorTotal').style.display='none';\r\n");

            out.write("\t\t\tdocument.getElementById('manutencao').style.display='block';\r\n");

            out.write("\t\t}else{\r\n");

            out.write("\t\t//\tdocument.getElementById('labelparcelas').style.display='block';\r\n");

            out.write("\t\t\t//document.getElementById('campoQuantidadeParcelas').style.display='block';\r\n");

            out.write("\t\t\tdocument.getElementById('parcelasDiv').style.display='block';\r\n");

            out.write("\t\t\tdocument.getElementById('valorMensal').style.display='none';\r\n");

            out.write("\t\t\tdocument.getElementById('valorTotal').style.display='block';\r\n");

            out.write("\t\t\tdocument.getElementById('manutencao').style.display='none';\r\n");

            out.write("\t\t}\r\n");

            out.write("\t\t\r\n");

            out.write("\t\tif(document.forms[0].radioTipoServico[4].checked){\r\n");

            out.write("\t\t\tdocument.getElementById('descricao').style.display='block';\r\n");

            out.write("\t\t}else{\r\n");

            out.write("\t\t\tdocument.getElementById('descricao').style.display='none';\r\n");

            out.write("\t\t}\r\n");

            out.write("\t\t\r\n");

            out.write("\t}\r\n");

            out.write("\t\r\n");

            out.write("\t\r\n");

            out.write("\tfunction abrir(aURL, W, L) {\r\n");
