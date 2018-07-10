                        break;

                    case BLUE:

                        {

                            alt3 = 2;

                        }

                        break;

                    case RED:

                        {

                            alt3 = 3;

                        }

                        break;

                    default:

                        if (state.backtracking > 0) {

                            state.failed = true;

                            return element;

                        }

                        NoViableAltException nvae = new NoViableAltException("", 3, 0, input);

                        throw nvae;

                }

                switch(alt3) {

                    case 1:

                        {

                            if (state.backtracking == 0) {

                            }

                            {

                                a20 = (Token) match(input, BLOND, FOLLOW_BLOND_in_parse_org_emftext_language_models_Model482);

                                if (state.failed) return element;

                                if (state.backtracking == 0) {

                                    if (terminateParsing) {

                                        throw new org.emftext.language.models.resource.model.mopp.ModelTerminateParsingException();

                                    }

                                    if (element == null) {

                                        element = org.emftext.language.models.ModelsFactory.eINSTANCE.createModel();

                                    }

                                    if (a20 != null) {

                                        org.emftext.language.models.resource.model.IModelTokenResolver tokenResolver = tokenResolverFactory.createTokenResolver("BLOND");

                                        tokenResolver.setOptions(getOptions());

                                        org.emftext.language.models.resource.model.IModelTokenResolveResult result = getFreshTokenResolveResult();

                                        tokenResolver.resolve(a20.getText(), element.eClass().getEStructuralFeature(org.emftext.language.models.ModelsPackage.MODEL__LIPS), result);

                                        java.lang.Object resolvedObject = result.getResolvedToken();

                                        if (resolvedObject == null) {

                                            addErrorToResource(result.getErrorMessage(), ((org.antlr.runtime.CommonToken) a20).getLine(), ((org.antlr.runtime.CommonToken) a20).getCharPositionInLine(), ((org.antlr.runtime.CommonToken) a20).getStartIndex(), ((org.antlr.runtime.CommonToken) a20).getStopIndex());

                                        }

                                        org.emftext.language.models.Color resolved = (org.emftext.language.models.Color) resolvedObject;

                                        if (resolved != null) {

                                            element.eSet(element.eClass().getEStructuralFeature(org.emftext.language.models.ModelsPackage.MODEL__LIPS), resolved);

                                        }

                                        collectHiddenTokens(element);

                                        copyLocalizationInfos((CommonToken) a20, element);

                                    }

                                }

                            }

                        }

                        break;

                    case 2:

                        {

                            if (state.backtracking == 0) {

                            }

                            {

                                a21 = (Token) match(input, BLUE, FOLLOW_BLUE_in_parse_org_emftext_language_models_Model511);

                                if (state.failed) return element;

                                if (state.backtracking == 0) {

                                    if (terminateParsing) {

                                        throw new org.emftext.language.models.resource.model.mopp.ModelTerminateParsingException();

                                    }

                                    if (element == null) {

                                        element = org.emftext.language.models.ModelsFactory.eINSTANCE.createModel();

                                    }

                                    if (a21 != null) {

                                        org.emftext.language.models.resource.model.IModelTokenResolver tokenResolver = tokenResolverFactory.createTokenResolver("BLUE");

                                        tokenResolver.setOptions(getOptions());

                                        org.emftext.language.models.resource.model.IModelTokenResolveResult result = getFreshTokenResolveResult();

                                        tokenResolver.resolve(a21.getText(), element.eClass().getEStructuralFeature(org.emftext.language.models.ModelsPackage.MODEL__LIPS), result);

                                        java.lang.Object resolvedObject = result.getResolvedToken();

                                        if (resolvedObject == null) {

                                            addErrorToResource(result.getErrorMessage(), ((org.antlr.runtime.CommonToken) a21).getLine(), ((org.antlr.runtime.CommonToken) a21).getCharPositionInLine(), ((org.antlr.runtime.CommonToken) a21).getStartIndex(), ((org.antlr.runtime.CommonToken) a21).getStopIndex());

                                        }

                                        org.emftext.language.models.Color resolved = (org.emftext.language.models.Color) resolvedObject;

                                        if (resolved != null) {

                                            element.eSet(element.eClass().getEStructuralFeature(org.emftext.language.models.ModelsPackage.MODEL__LIPS), resolved);

                                        }

                                        collectHiddenTokens(element);

                                        copyLocalizationInfos((CommonToken) a21, element);

                                    }

                                }

                            }

                        }

                        break;

                    case 3:

                        {

                            if (state.backtracking == 0) {

                            }

                            {

                                a22 = (Token) match(input, RED, FOLLOW_RED_in_parse_org_emftext_language_models_Model540);

                                if (state.failed) return element;

                                if (state.backtracking == 0) {

                                    if (terminateParsing) {

                                        throw new org.emftext.language.models.resource.model.mopp.ModelTerminateParsingException();

                                    }

                                    if (element == null) {

                                        element = org.emftext.language.models.ModelsFactory.eINSTANCE.createModel();

                                    }

                                    if (a22 != null) {

                                        org.emftext.language.models.resource.model.IModelTokenResolver tokenResolver = tokenResolverFactory.createTokenResolver("RED");

                                        tokenResolver.setOptions(getOptions());

                                        org.emftext.language.models.resource.model.IModelTokenResolveResult result = getFreshTokenResolveResult();

                                        tokenResolver.resolve(a22.getText(), element.eClass().getEStructuralFeature(org.emftext.language.models.ModelsPackage.MODEL__LIPS), result);

                                        java.lang.Object resolvedObject = result.getResolvedToken();

                                        if (resolvedObject == null) {

                                            addErrorToResource(result.getErrorMessage(), ((org.antlr.runtime.CommonToken) a22).getLine(), ((org.antlr.runtime.CommonToken) a22).getCharPositionInLine(), ((org.antlr.runtime.CommonToken) a22).getStartIndex(), ((org.antlr.runtime.CommonToken) a22).getStopIndex());

                                        }

                                        org.emftext.language.models.Color resolved = (org.emftext.language.models.Color) resolvedObject;

                                        if (resolved != null) {

                                            element.eSet(element.eClass().getEStructuralFeature(org.emftext.language.models.ModelsPackage.MODEL__LIPS), resolved);

                                        }

                                        collectHiddenTokens(element);

                                        copyLocalizationInfos((CommonToken) a22, element);

                                    }

                                }

                            }

                        }

                        break;

                }

                if (state.backtracking == 0) {

                }

                a23 = (Token) match(input, 22, FOLLOW_22_in_parse_org_emftext_language_models_Model562);

                if (state.failed) return element;

                if (state.backtracking == 0) {

                    if (element == null) {

                        element = org.emftext.language.models.ModelsFactory.eINSTANCE.createModel();

                    }

                    collectHiddenTokens(element);

                    copyLocalizationInfos((CommonToken) a23, element);

                }

                if (state.backtracking == 0) {

                }

                a24 = (Token) match(input, 20, FOLLOW_20_in_parse_org_emftext_language_models_Model573);

                if (state.failed) return element;

                if (state.backtracking == 0) {

                    if (element == null) {

                        element = org.emftext.language.models.ModelsFactory.eINSTANCE.createModel();

                    }

                    collectHiddenTokens(element);
