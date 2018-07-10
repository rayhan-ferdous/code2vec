            out.write("\t}\r\n");

            out.write("\t\r\n");

            out.write("\tfunction adicionar(){\r\n");

            out.write("\t\tvar form = document.forms[0];\r\n");

            out.write("\t\tform.metodo.value = 'add';\r\n");

            out.write("\t\tform.submit();\r\n");

            out.write("\t}\r\n");

            out.write("\t\r\n");

            out.write("\t\r\n");

            out.write("\tfunction editar() {\r\n");

            out.write("\t\tvar form = document.forms[0];\r\n");

            out.write("\t\tform.metodo.value = 'editar';\r\n");

            out.write("\t\tform.submit();\r\n");

            out.write("\t}\r\n");

            out.write("\t\r\n");

            out.write("\r\n");

            out.write("\tfunction carregarMascaras() {\r\n");

            out.write("\t\tdocument.formCadastroServico.reset();\r\n");

            out.write("\t\t\r\n");

            out.write("\t\tvalorMask = new Mask(\"#.00\", \"number\");\r\n");

            out.write("\t\tvalorMask.attach(document.formCadastroServico.valor);\r\n");

            out.write("\t\t\r\n");

            out.write("\t\tquantidadeParcelasMask = new Mask(\"#\", \"number\");\r\n");

            out.write("\t\tquantidadeParcelasMask.attach(document.formCadastroServico.quantidadeParcelas);\r\n");

            out.write("\t\t\r\n");

            out.write("\t\tmesMask = new Mask(\"##\", \"number\");\r\n");

            out.write("\t\tmesMask.attach(document.formCadastroServico.mesVencimentoPrimeiraParcela);\r\n");

            out.write("\t\tmesMask.attach(document.formCadastroServico.mesIsencao);\r\n");

            out.write("\t\t\r\n");
