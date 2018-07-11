                        schemaManager.addSchemaObject(routine);

                        break;

                    } catch (HsqlException e) {

                        return Result.newErrorResult(e, sql);

                    }

                }

            case StatementTypes.CREATE_ALIAS:

                {

                    HsqlName name = (HsqlName) arguments[0];

                    Routine[] routines = (Routine[]) arguments[1];
