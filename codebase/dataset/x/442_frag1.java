            out.write("\t\t\t\t\t\t<td align=\"right\">行驶证发证日期：</td>\r\n");

            out.write("\t\t\t\t\t\t<td><input name=\"fzRq\" id=\"fzRq\" onFocus=\"WdatePicker()\" class=\"required\" />\r\n");

            out.write("\t\t\t\t\t\t\t<span style=\"color: red;\">*</span>\r\n");

            out.write("\t\t\t\t\t\t</td>\r\n");

            out.write("\t\t\t\t\t</tr>\r\n");

            out.write("\t\t\t\t\t<tr onMouseOver=\"mouseOver(this)\" onMouseOut=\"mouseOut(this)\">\r\n");

            out.write("\t\t\t\t\t\t<td align=\"right\">核定载重量（吨）：</td>\r\n");

            out.write("\t\t\t\t\t\t<td><input name=\"hdzzl\" id=\"hdzzl\" readonly=\"readonly\" onblur=\"CheckValue(this)\" onkeypress=\"onlynum(this)\" class=\"required\" />\r\n");

            out.write("\t\t\t\t\t\t\t<span style=\"color: red;\">*</span>\r\n");

            out.write("\t\t\t\t\t\t</td>\r\n");

            out.write("\t\t\t\t\t\t<td align=\"right\">整备质量（吨）：</td>\r\n");

            out.write("\t\t\t\t\t\t<td><input name=\"zbzl\" id=\"zbzl\" readonly=\"readonly\" onblur=\"CheckValue(this)\" onkeypress=\"onlynum(this)\" class=\"required\" />\r\n");

            out.write("\t\t\t\t\t\t\t<span style=\"color: red;\">*</span>\r\n");

            out.write("\t\t\t\t\t\t</td>\r\n");

            out.write("\t\t\t\t\t</tr>\r\n");

            out.write("\t\t\t\t\t<tr onMouseOver=\"mouseOver(this)\" onMouseOut=\"mouseOut(this)\">\r\n");

            out.write("\t\t\t\t\t\t<td align=\"right\">核定载客（人）：</td>\r\n");

            out.write("\t\t\t\t\t\t<td><input name=\"hdzkl\" id=\"hdzkl\" readonly=\"readonly\" onblur=\"CheckValue(this)\" onkeypress=\"onlydigit(this)\" class=\"required\" />\r\n");

            out.write("\t\t\t\t\t\t\t<span style=\"color: red;\">*</span>\r\n");

            out.write("\t\t\t\t\t\t</td>\r\n");

            out.write("\t\t\t\t\t\t<td align=\"right\">排气量（升）：</td>\r\n");

            out.write("\t\t\t\t\t\t<td><input name=\"pql\" id=\"pql\" readonly=\"readonly\" onblur=\"CheckValue(this)\" onkeypress=\"onlynum(this)\" class=\"required\" />\r\n");

            out.write("\t\t\t\t\t\t\t<span style=\"color: red;\">*</span>\r\n");

            out.write("\t\t\t\t\t\t</td>\r\n");

            out.write("\t\t\t\t\t</tr>\r\n");

            out.write("\t\t\t\t\t<tr onMouseOver=\"mouseOver(this)\" onMouseOut=\"mouseOut(this)\">\r\n");

            out.write("\t\t\t\t\t\t<td align=\"right\">登记日期：</td>\r\n");

            out.write("\t\t\t\t\t\t<td><input name=\"djRq\" id=\"djRq\" onFocus=\"WdatePicker({onpicked:function(){gdt('system/book/car_register_gdt.do',this,gzRq,sbqsny);}})\" onblur=\"gdt('system/book/car_register_gdt.do',this,gzRq,sbqsny)\" class=\"required\" />\r\n");

            out.write("\t\t\t\t\t\t\t<span style=\"color: red;\">*</span>\r\n");

            out.write("\t\t\t\t\t\t</td>\r\n");

            out.write("\t\t\t\t\t\t<td align=\"right\">购置时间：</td>\r\n");

            out.write("\t\t\t\t\t\t<td><input name=\"gzRq\" id=\"gzRq\" onFocus=\"WdatePicker({onpicked:function(){gdt('system/book/car_register_gdt.do',djRq,this,sbqsny);}})\" onblur=\"gdt('system/book/car_register_gdt.do',djRq,this,sbqsny)\" class=\"required\" />\r\n");

            out.write("\t\t\t\t\t\t\t<span style=\"color: red;\">*</span>\r\n");

            out.write("\t\t\t\t\t\t</td>\r\n");

            out.write("\t\t\t\t\t</tr>\r\n");

            out.write("\t\t\t\t\t<tr onMouseOver=\"mouseOver(this)\" onMouseOut=\"mouseOut(this)\">\r\n");

            out.write("\t\t\t\t\t\t<td align=\"right\">申报起始年月：</td>\r\n");

            out.write("\t\t\t\t\t\t<td><input name=\"sbqsny\" id=\"sbqsny\" readonly=\"readonly\" /></td>\r\n");

            out.write("\t\t\t\t\t\t<td align=\"right\"></td>\r\n");

            out.write("\t\t\t\t\t\t<td></td>\r\n");

            out.write("\t\t\t\t\t</tr>\r\n");

            out.write("\t\t\t\t\t<tr onMouseOver=\"mouseOver(this)\" onMouseOut=\"mouseOut(this)\">\r\n");

            out.write("\t\t\t\t\t\t<td align=\"right\">备注：</td>\r\n");

            out.write("\t\t\t\t\t\t<td colspan=\"3\">\r\n");

            out.write("\t\t\t\t\t\t\t<textarea name=\"bz\" id=\"bz\" cols=\"50\" rows=\"3\"></textarea>\r\n");

            out.write("\t\t\t\t\t\t\t非必填，长度不能超过100 \r\n");

            out.write("\t\t\t\t\t\t</td>\r\n");

            out.write("\t\t\t\t\t</tr>\r\n");

            out.write("\t\t\t\t\t<tr onMouseOver=\"mouseOver(this)\" onMouseOut=\"mouseOut(this)\">\r\n");

            out.write("\t\t\t\t\t\t<td align=\"right\"></td>\r\n");

            out.write("\t\t\t\t\t\t<td colspan=\"3\"><input type=\"checkbox\" name=\"next\" id=\"next\" />保存成功后登记同一车主下一车辆</td>\r\n");

            out.write("\t\t\t\t\t</tr>\r\n");

            out.write("\t\t\t\t\t<tr>\r\n");

            out.write("\t\t\t\t\t\t<td colspan=\"4\" class=\"form_button\" style=\"padding-top:10px;\">\r\n");

            out.write("\t\t\t\t\t\t\t<input type=\"submit\" value=\"保存并继续登记\" />\r\n");

            out.write("\t\t\t\t\t\t\t<input type=\"submit\" value=\"保存并申报\" />\r\n");

            out.write("\t\t\t\t\t\t\t<input type=\"reset\" value=\"重置\" />\r\n");

            out.write("\t\t\t\t\t\t</td>\r\n");

            out.write("\t\t\t\t\t</tr>\r\n");

            out.write("\t\t\t\t</table>\r\n");

            out.write("\t\t\t\t<div id=\"divProvince\" name=\"divProvince\" style=\"display:none; position:absolute;width:260px;background-color:#BFEBEE; border:1px solid #BEC0BF;padding:5px;font-size:12px;\">  \r\n");

            out.write("\t\t\t\t        电话格式 ：<br/>手机：18,15,13开头 + 9位。<br/>固话：区号-电话。 例 ： 0760-88888888\r\n");

            out.write("\t\t\t\t</div>\r\n");

            out.write("\t\t\t</form>\r\n");

            out.write("\t\t</div>\r\n");

            out.write("\t</div>\r\n");

            out.write("</div>\r\n");

            out.write("<script type=\"text/javascript\">\r\n");

            out.write("\tfunction CZLBChange(value){\r\n");

            out.write("\t\tif(value==\"\"){\r\n");

            out.write("\t\t\t$('#nsrbm').attr(\"value\",'');\r\n");

            out.write("\t\t\t$('#nsrbm').attr(\"readonly\",false);\r\n");

            out.write("\t\t\t$('#sfzhm').attr(\"value\",'');\r\n");

            out.write("\t\t\t$('#sfzhm').attr(\"readonly\",false);\r\n");

            out.write("\t\t\t$('#czMc').attr(\"value\",'');\r\n");

            out.write("\t\t\t$('#czDz').attr(\"value\",'');\r\n");

            out.write("\t\t\t$('#czDh').attr(\"value\",'');\r\n");

            out.write("\t\t}\r\n");

            out.write("\t\tif(value==\"01\"){\r\n");

            out.write("\t\t\t$('#nsrbm').attr(\"value\",'');\r\n");
