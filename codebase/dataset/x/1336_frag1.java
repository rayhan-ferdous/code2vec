    private OpBackupMember getMemberForRelationShip(OpRelationship relationship, List<Field> prefix, List<String> namePrefix) {

        OpBackupMember backupMember = new OpBackupMember();

        backupMember.setNames(namePrefix, relationship.getName());

        backupMember.typeId = relationship.getTypeID();

        backupMember.relationship = true;

        backupMember.ordered = false;

        backupMember.recursive = relationship.getRecursive();

        OpRelationship backRelationShip = relationship.getBackRelationship();

        backupMember.backRelationshipName = backRelationShip != null ? backRelationShip.getName() : null;

        return backupMember;

    }
