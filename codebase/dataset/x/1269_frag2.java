        Node createChild(String child_name, String fqn, Node parent, HashMap<String, Object> data) {

            Node child = null;

            if (child_name == null) return null;

            if (children == null) children = new TreeMap<String, Node>();

            child = children.get(child_name);

            if (child != null) child.setData(data); else {

                child = new Node(child_name, fqn, parent, data);

                children.put(child_name, child);

            }

            return child;

        }
