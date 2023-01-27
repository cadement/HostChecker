package com.caseydement;

import java.util.*;

public class HostChecker {

    private static int NUM_MATCH_RUNS = 0;
    private static int NUM_MATCH_STEPS = 0;

    private static int GETINDEX(int character) {
        return character - 96;
    }

    private static class Node {

        private String host;
        private final List<Node> children = new ArrayList<>(27);

        public Node(String host) {
            this.host = host;
            for (int i = 0; i < 27; i++) {
                children.add(null);
            }
        }

        public void addHost(String host) {
            int index = GETINDEX(host.charAt(0));
            String rest = host.substring(1);
            Node node = children.get(index);
            if (node != null) {
                node.addHost(rest);
            } else {
                children.set(index, new Node(rest));
            }

            if (null != this.host) {
                String tmp = this.host;
                this.host = null;
                this.addHost(tmp);
            }
        }

        public Boolean matchHost(String host) {
            NUM_MATCH_STEPS++;
            if (null != this.host) {
                return host.matches(this.host);
            } else {
                int index = GETINDEX(host.charAt(0));
                String rest = host.substring(1);
                Node node = children.get(index);
                if (node != null) {
                    return node.matchHost(rest);
                } else {
                    return false;
                }
            }
        }
    }

    private final Map<String, Node> nodes = new HashMap<String, Node>();

    public HostChecker() {}

    public void addHost(String host) {
        String chars = new StringBuffer(host).reverse().toString();
        int dotAt = chars.indexOf(".");
        String tld = chars.substring(0, dotAt);
        String rest = chars.substring(dotAt + 1).replace(".", "`").replace("*", ".*");
        Node node = this.nodes.get(tld);
        if (null != node) {
            node.addHost(rest);
        } else {
            this.nodes.put(tld, new Node(rest));
        }
    }

    public Boolean matchHost(String host) {
        NUM_MATCH_RUNS++;
        NUM_MATCH_STEPS++;
        String chars = new StringBuffer(host).reverse().toString();
        int dotAt = chars.indexOf(".");
        String tld = chars.substring(0, dotAt);
        String rest = chars.substring(dotAt + 1).replace(".", "`");
        Node node = this.nodes.get(tld);
        if (null != node) {
            return node.matchHost(rest);
        } else {
            return false;
        }
    }

    public float stepsPerRun() {
        return (float) NUM_MATCH_STEPS / (float) NUM_MATCH_RUNS;
    }

    public int getNumRuns() {
        return NUM_MATCH_RUNS;
    }
}

