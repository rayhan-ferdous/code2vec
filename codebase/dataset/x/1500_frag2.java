            out.write(";\r\n");

            out.write("\t\tif(b) {\r\n");

            out.write("\t\t\talert('Não é possível remover procedimentos de uma FIOD já cadastrada.');\r\n");

            out.write("\t\t\treturn;\r\n");

            out.write("\t\t}\r\n");

            out.write("\t    document.getElementById('idProcedimento').value = id;\r\n");

            out.write("\t\tvar form = document.forms[0];\r\n");

            out.write("\t\tform.metodo.value = 'remover';\r\n");

            out.write("\t\tform.submit();\r\n");

            out.write("\t}\r\n");

            out.write("\t\r\n");

            out.write("\tfunction inicializaPaciente(){\r\n");

            out.write("\t\tdocument.getElementById('model').value = document.getElementById('nomePaciente').value;\r\n");

            out.write("\t}\r\n");
