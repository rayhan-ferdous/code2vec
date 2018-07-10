    public RestServiceResult create(RestServiceResult serviceResult, CoParagraphBaseKnowledge coParagraphBaseKnowledge) {

        CoParagraphBaseKnowledgeDAO coParagraphBaseKnowledgeDAO = new CoParagraphBaseKnowledgeDAO();

        try {

            coParagraphBaseKnowledge.setKnowledgeId(getSequence("sq_co_paragraph_base_knowledge"));

            EntityManagerHelper.beginTransaction();

            coParagraphBaseKnowledgeDAO.save(coParagraphBaseKnowledge);

            EntityManagerHelper.commit();

            EntityManagerHelper.refresh(coParagraphBaseKnowledge);

            log.info("Knowledge creado con �xito: " + coParagraphBaseKnowledge.getKnowledgeId());

            Object[] arrayParam = { coParagraphBaseKnowledge.getKnowledgeId() };

            serviceResult.setMessage(MessageFormat.format(bundle.getString("paragraphBaseKnowledge.create.success"), arrayParam));

        } catch (PersistenceException e) {

            EntityManagerHelper.rollback();

            log.error("Error al guardar el knowledge: " + e.getMessage());

            serviceResult.setError(true);

            serviceResult.setMessage(MessageFormat.format(bundle.getString("paragraphBaseKnowledge.create.error"), e.getMessage()));

        }

        return serviceResult;

    }
