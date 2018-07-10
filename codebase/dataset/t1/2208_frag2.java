    public static Extern findPackage(String pkgName) {

        if (packageMap == null) {

            return null;

        }

        return (Extern) packageMap.get(pkgName);

    }
