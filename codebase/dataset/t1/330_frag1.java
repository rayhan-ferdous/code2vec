            para.addTextAsRunWithParagraphSetting("headline");

            docxSource1.appendParagraph(para);

            para = paraBuilderNormalText.newParagraph();

            para.addTextAsRunWithParagraphSetting("introduction");

            docxSource1.appendParagraph(para);

            File destFile = new File(outputDirectory + fileName);

            assertTrue(docxSource1.save(destFile));

            OpenXmlAssert.assertEquals(new File(outputDirectory + fileName), new File(expectedResult));

        } catch (IOException e) {

            logger.error(e);

            fail("cannot open:" + fileInput);

        } catch (OpenXML4JException e) {

            logger.error(e);

            fail("cannot open:" + fileInput);

        }

    }
